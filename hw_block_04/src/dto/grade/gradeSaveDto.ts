export class GradeSaveDto {
    studentId?: number;
    value?: number;
    date?: Date;

    constructor(data: Partial<GradeSaveDto>) {
        this.value = data.value;
        this.date = data.date;
        this.studentId = data.studentId;
    }
}
