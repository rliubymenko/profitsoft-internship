import express from 'express';
import grades from './grades';

const router = express.Router();

router.use('/api/grades', grades);

export default router;
