import {StudentDetailsDto} from 'src/dto/student/studentDetailsDto';
import {GradeSaveDto} from "src/dto/grade/gradeSaveDto";
import Grade, {IGrade} from "src/model/grade";
import config from "src/config";
import log4js from "log4js";
import {GradeInfoDto} from "src/dto/grade/gradeInfoDto";
import {GradeQueryDto} from "src/dto/grade/gradeQueryDto";

export const createGrade = async (gradeDto: GradeSaveDto): Promise<string> => {
    await validateGrade(gradeDto);
    const grade = await new Grade(gradeDto).save();
    return grade._id;
}

const validateGrade = async (gradeDto: GradeSaveDto) => {
    const studentId = gradeDto.studentId;
    if (studentId) {
        const student = await getStudentById(studentId)

        if (!student) {
            throw new Error(`Student with id ${studentId} doesn't exist`);
        }
    }
    if (!gradeDto.date) {
        throw new Error("Date of the grade is required");
    }
    if (!gradeDto.value) {
        throw new Error("Value of the grade is required");
    }
};

export const getStudentById = async (id: number): Promise<StudentDetailsDto> => {
    return await fetch(`${config.backEnd.url}/students/${id}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Error while trying to connect to ${config.backEnd.url}`);
            }
            return response.json();
        })
        .then(student => {
            return student;
        })
        .catch(error => {
            log4js.getLogger().warn(error);
        });
}

export const getGrades = async (query: GradeQueryDto): Promise<GradeInfoDto[]> => {
    const {studentId} = query;

    if (!studentId) {
        throw new Error("studentId is required");
    }

    const grades = await Grade
        .find({
            "studentId": studentId
        })
        .sort({"date": "desc"})
        .skip(query.from)
        .limit(query.size);

    return grades.map(grade => toInfoDto(grade));
};

const toInfoDto = (grade: IGrade): GradeInfoDto => {
    return ({
        _id: grade._id,
        value: grade.value,
        date: grade.date,
        studentId: grade.studentId,
    });
};


export const countGrades = async (studentIds: number[]): Promise<({ [p: number]: any } | {
    [p: number]: number
})[]> => {
    const grades = await Grade.aggregate([
        {
            $match: {
                studentId: {$in: studentIds}
            }
        },
        {
            $group: {
                _id: "$studentId",
                count: {$sum: 1},
            }
        }
    ]);

    return studentIds.map(studentId => {
        const grade = grades.find(grade => grade._id === studentId);
        if (grade) {
            return {[studentId]: grade.count}
        }
        return {[studentId]: 0}
    })
}
