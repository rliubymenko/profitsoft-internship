import mongoose, {Document, Schema} from 'mongoose';

export interface IGrade extends Document {
    /** will be added by mongo */
    createdAt: Date;
    updatedAt: Date;
    // entity's properties
    value: number;
    date: Date;
    studentId: number;
}

const studentSchema = new Schema({
        value: {
            required: [true, 'Must have a mark'],
            type: Number,
            min: [1, "Mark must be bigger than 0"],
            max: [100, "Mark must be less than 100"]
        },
        date: {
            required: true,
            type: Date,
        },
        studentId: {
            required: true,
            type: Number
        }
    },
    {
        /**
         * The timestamps option tells mongoose to assign createdAt and updatedAt
         * fields to your schema. The type assigned is Date.
         */
        timestamps: true,
        timezone: 'UTC',
    },
);

const Grade = mongoose.model<IGrade>('Grade', studentSchema);

export default Grade;
