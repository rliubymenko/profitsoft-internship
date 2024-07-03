const config = {
    // Services
    USERS_SERVICE: process.env.REACT_APP_BACK_SERVICE ? `${process.env.REACT_APP_BACK_SERVICE}/api/v1` : '',
    UI_URL_PREFIX: process.env.REACT_APP_UI_URL_PREFIX || '',
};

export default config;
