import bodyParser from 'body-parser';
import express from 'express';
import sinon from 'sinon';
import chai from 'chai';
import chaiHttp from 'chai-http';
import routers from "src/routers";
import * as gradeService from 'src/services/grade';
import {GradeInfoDto} from "../../dto/grade/gradeInfoDto";

const {expect} = chai;

chai.use(chaiHttp);
chai.should();

const sandbox = sinon.createSandbox();

const app = express();

app.use(bodyParser.json({limit: '1mb'}));
app.use('', routers);

describe('Grade Controller Test', () => {

    afterEach(() => {
        sandbox.restore();
    });

    it('should save the grade', (done) => {
        const grade = {
            value: 5,
            date: new Date(),
            studentId: 8
        };

        const createStub = sandbox.stub(gradeService, 'createGrade');
        createStub.resolves("id");

        chai.request(app)
            .post('/api/grades')
            .send(grade)
            .end((_, res) => {
                res.should.have.status(201);
                expect(res.body.id).to.equal("id");
                done();
            });
    })

    it('should get a grade for the student', (done) => {
        const grades: GradeInfoDto[] = [{
            _id: "id",
            value: 5,
            date: new Date('2021-01-01T00:00:00.000Z'),
            studentId: 8,
        }]

        const getGrades = sandbox.stub(gradeService, 'getGrades');
        getGrades.resolves(grades);

        chai.request(app)
            .get('/api/grades')
            .query({
                from: 0,
                size: 10,
                studentId: 8
            })
            .end((_, res) => {
                res.should.have.status(200);

                done();
            });
    })

    it('should count grades', (done) => {
        const studentIds = [8]
        const expectedResult = [{"8": 2}]

        const countGrades = sandbox.stub(gradeService, 'countGrades');
        countGrades.resolves(expectedResult);

        chai.request(app)
            .post('/api/grades/_counts')
            .send(studentIds)
            .end((_, res) => {
                res.should.have.status(200);
                expect(res.body.result).to.deep.equal(expectedResult);

                done();
            });
    })
});