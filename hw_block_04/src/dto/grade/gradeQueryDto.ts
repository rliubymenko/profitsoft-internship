import {QueryDto} from 'src/dto/queryDto';

export class GradeQueryDto extends QueryDto {
    studentId?: number;

    constructor(query?: Partial<GradeQueryDto>) {
        super();
        if (query) {
            this.studentId = query.studentId;
            this.from = query.from ?? 0;
            this.size = query.size ?? 10;
        }
    }
}
