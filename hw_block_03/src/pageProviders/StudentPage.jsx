import React from 'react';
import PageContainer from './components/PageContainer';
import Students from "../pages/students/containers/Students";

const StudentsPage = (props) => {
    return (
        <PageContainer>
            <Students {...props} />
        </PageContainer>
    );
};

export default StudentsPage;
