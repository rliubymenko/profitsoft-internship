const config = {
    consul: {
        server: {
            prod: {
                host: 'consul',
                port: '8500',
            },
            dev: {
                host: 'localhost',
                port: '8501',
            },
        },
        service: {
            name: 'nodejs-sample',
        },
    },
    log4js: {
        appenders: {
            console: {
                type: 'console',
            },
            ms: {
                type: 'dateFile',
                pattern: '-yyyy-MM-dd.log',
                alwaysIncludePattern: true,
                filename: 'log/ms',
                maxLogSize: 1000000,
                compress: true,
            },
        },
        categories: {
            default: {
                appenders: ['ms', 'console'],
                level: 'debug',
            },
        },
    },
    backEnd: {
        // I didn't add anything to docker compose file, instead I found Ipv4 via ipconfig and run locally backend
        url: 'http://192.168.178.34:8080/api/v1'
    }
};

export default config;
