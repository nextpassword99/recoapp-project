package com.example.recoapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {
    private lateinit var previewView: PreviewView
    private lateinit var tvStatus: TextView
    private lateinit var btnClose: MaterialButton

    private lateinit var cameraExecutor: ExecutorService
    private val labeler by lazy { ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS) }

    private var lastAnalyzeTs = 0L

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) startCamera() else Toast.makeText(this, getString(R.string.camera_permission_required), Toast.LENGTH_LONG).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        previewView = findViewById(R.id.previewView)
        tvStatus = findViewById(R.id.tvStatus)
        btnClose = findViewById(R.id.btnClose)

        btnClose.setOnClickListener { finish() }

        cameraExecutor = Executors.newSingleThreadExecutor()

        if (hasCameraPermission()) startCamera() else requestCameraPermission()
    }

    private fun hasCameraPermission(): Boolean =
        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private fun requestCameraPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also { it.setSurfaceProvider(previewView.surfaceProvider) }

            val analyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { analysis ->
                    analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                        analyzeImage(imageProxy)
                    }
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, analyzer)
            } catch (exc: Exception) {
                tvStatus.text = "Error iniciando cámara: ${exc.message}"
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun analyzeImage(imageProxy: ImageProxy) {
        val now = System.currentTimeMillis()
        if (now - lastAnalyzeTs < 500) {
            imageProxy.close()
            return
        }
        lastAnalyzeTs = now

        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        labeler.process(image)
            .addOnSuccessListener { labels ->
                if (labels.isNotEmpty()) {
                    val best = labels.maxByOrNull { it.confidence }
                    if (best != null) {
                        val mapped = mapLabelToWasteType(best.text)
                        tvStatus.text = "${best.text} (${"%.2f".format(best.confidence)}) -> ${mapped ?: "-"}"
                        if (mapped != null && best.confidence >= 0.6f) {
                            returnWithResult(mapped)
                        }
                    } else {
                        tvStatus.text = getString(R.string.camera_detecting)
                    }
                } else {
                    tvStatus.text = getString(R.string.camera_detecting)
                }
            }
            .addOnFailureListener {
                tvStatus.text = "Error: ${it.message}"
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    private fun mapLabelToWasteType(label: String): String? {
        val l = label.lowercase()
        return when {
            listOf("plastic", "plastics").any { l.contains(it) } -> getString(R.string.plastic)
            listOf("paper", "cardboard").any { l.contains(it) } -> getString(R.string.paper)
            listOf("glass", "bottle").any { l.contains(it) } -> getString(R.string.glass)
            listOf("organic", "food", "fruit", "vegetable", "biodegradable").any { l.contains(it) } -> getString(R.string.organic)
            else -> null
        }
    }

    private fun returnWithResult(typeDisplay: String) {
        val data = Intent().apply { putExtra(EXTRA_DETECTED_TYPE, typeDisplay) }
        setResult(RESULT_OK, data)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::cameraExecutor.isInitialized) cameraExecutor.shutdown()
    }

    companion object {
        const val EXTRA_DETECTED_TYPE = "detected_type"
    }
}
