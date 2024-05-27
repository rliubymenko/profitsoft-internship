import express from 'express';
import {countGradesByStudents, listGradesByStudentId, saveGrade} from 'src/controllers/grades';

const router = express.Router();

router.post('', saveGrade);
router.get('', listGradesByStudentId);
router.post('/_counts', countGradesByStudents);

export default router;
