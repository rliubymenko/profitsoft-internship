import React from 'react';
import PageContainer from './components/PageContainer';
import StudentProfile from "../pages/student-profile/containers/StudentProfile";

const StudentProfilePage = (props) => {
    return (
        <PageContainer>
            <StudentProfile {...props} />
        </PageContainer>
    );
};

export default StudentProfilePage;
