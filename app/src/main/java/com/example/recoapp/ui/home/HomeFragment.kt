package com.example.recoapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.recoapp.databinding.FragmentHomeBinding

/**
 * Fragment que muestra la pantalla de inicio/bienvenida de RecoApp
 * 
 * Pantalla principal donde se muestra información general de la aplicación
 * y accesos rápidos a las funcionalidades principales para ECOLIM S.A.C.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    
    // Esta propiedad solo es válida entre onCreateView y onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Observar datos del ViewModel
        homeViewModel.text.observe(viewLifecycleOwner) {
            binding.textHome.text = it
        }
        
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}