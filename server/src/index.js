import dotenv from 'dotenv';
import express from 'express';
import cors from 'cors';
import { sequelize } from './lib/database.js';
import './models/User.js';
import authRouter from './routes/auth.js';

dotenv.config();

const app = express();
app.use(cors());
app.use(express.json());

app.get('/health', (_, res) => res.json({ ok: true }));
app.use('/api/auth', authRouter);

const PORT = process.env.PORT || 4000;

async function start() {
  try {
    await sequelize.sync();
    app.listen(PORT, () => {
      console.log(`Server listening on http://localhost:${PORT}`);
    });
  } catch (err) {
    console.error('Failed to start server', err);
    process.exit(1);
  }
}

if (process.argv.includes('--init-db')) {
  (async () => {
    try {
      await sequelize.sync({ force: true });
      console.log('Database initialized');
      process.exit(0);
    } catch (e) {
      console.error(e);
      process.exit(1);
    }
  })();
} else {
  start();
}
