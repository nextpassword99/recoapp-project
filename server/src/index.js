import dotenv from "dotenv";
import express from "express";
import cors from "cors";
import { sequelize } from "./lib/database.js";
import "./models/User.js";
import authRouter from "./routes/auth.js";

dotenv.config();

const app = express();
app.use(cors());
app.use(express.json());

sequelize.sync().catch((err) => {
  console.error("Failed to sync database", err);
});

app.get("/", (req, res) => {
  res.send("<h1>API de RecoApp funcionando!</h1>");
});

app.get("/health", (_, res) => res.json({ ok: true }));
app.use("/api/auth", authRouter);

export default app;
