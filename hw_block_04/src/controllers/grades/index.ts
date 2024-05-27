import log4js from 'log4js';
import httpStatus from 'http-status';
import {Request, Response} from 'express';
import {countGrades, createGrade, getGrades} from 'src/services/grade';
import {InternalError} from 'src/system/internalError';
import {GradeSaveDto} from "src/dto/grade/gradeSaveDto";
import {GradeQueryDto} from "src/dto/grade/gradeQueryDto";

export const saveGrade = async (req: Request, res: Response) => {
    try {
        const grade = new GradeSaveDto(req.body);
        const id = await createGrade({
            ...grade,
        });
        res.status(httpStatus.CREATED).send({
            id,
        });
    } catch (err) {
        const {message, status} = new InternalError(err);
        log4js.getLogger().error('Error in creating grade', err);
        res.status(status).send({message});
    }
};

export const listGradesByStudentId = async (req: Request, res: Response) => {
    try {
        const result = await getGrades(new GradeQueryDto(req.query));
        res.send({
            result,
        });
    } catch (err) {
        const {message, status} = new InternalError(err);
        log4js.getLogger().error(`Error in retrieving grades`, err);
        res.status(status).send({message});
    }
};

export const countGradesByStudents = async (req: Request, res: Response) => {
    try {
        const result = await countGrades(req.body.studentIds ?? []);
        res.send({
            result,
        });
    } catch (err) {
        const {message, status} = new InternalError(err);
        log4js.getLogger().error(`Error in searching grades`, err);
        res.status(status).send({message});
    }
};
