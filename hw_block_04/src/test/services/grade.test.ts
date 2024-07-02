import chai from 'chai';
import sinon from 'sinon';
import mongoSetup from 'src/test/mongoSetup';
import Grade from "src/model/grade";
import {GradeSaveDto} from "src/dto/grade/gradeSaveDto";
import * as gradeService from 'src/services/grade';
import {GradeQueryDto} from "src/dto/grade/gradeQueryDto";
import {StudentDetailsDto} from "../../dto/student/studentDetailsDto";

const {expect} = chai;
const sandbox = sinon.createSandbox();

const grade = new Grade({
    value: 5,
    date: new Date('2021-01-01'),
    studentId: 8,
})

describe('Grade Service Test', () => {
    before(async () => {
        /**
         * The mongoSetup promise is resolved when the database is ready to be used.
         * After it is resolved we can save all the needed data.
         */
        await mongoSetup;
        await grade.save();
    });

    it("should create grade", (done) => {
        const gradeDto: GradeSaveDto = {
            value: 5,
            date: new Date('2021-01-01'),
            studentId: 8,
        }
        const getStudentById = sandbox.stub(gradeService, 'getStudentById');
        const student: StudentDetailsDto = {
            id: 8
        }
        getStudentById.resolves(student)

        gradeService.createGrade(gradeDto)
            .then(async (id) => {
                expect(id).to.exist;
                const grade = await Grade.findById(id);
                expect(grade).to.exist;
                expect(grade?.value).to.equal(gradeDto.value);
                expect(grade?.date).to.eql(gradeDto.date);
                expect(grade?.studentId).to.eql(gradeDto.studentId);
                done();
            })
            .catch((error: Error) => done(error));
    })

    it("should get grades by the query", (done) => {
        const query: GradeQueryDto = {
            studentId: 8,
            from: 0,
            size: 10,
        }

        gradeService.getGrades(query)
            .then(async (grades) => {
                expect(grades).to.exist
                expect(grades.length).to.equal(2);
                expect(grades[0]).to.have.property('value', grade.value);
                expect(grades[0]).to.have.property('date').that.eql(grade.date);
                expect(grades[0]).to.have.property('studentId', grade.studentId);
                done();
            })
            .catch((error: Error) => done(error));
    })

    it("should count grades for student ids", (done) => {
        const studentIds = [8]

        gradeService.countGrades(studentIds)
            .then(async (statistics) => {
                expect(statistics).to.exist;
                expect(statistics.length).to.equal(studentIds.length);
                expect(statistics).to.have.deep.members([{'8': 2}])
                done();
            })
            .catch((error: Error) => done(error));
    })
});
