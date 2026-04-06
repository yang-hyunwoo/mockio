import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    scenarios: {
        signup_test: {
            executor: 'constant-vus',
            vus: 100,
            duration: '30s'
        },
    },
};

export default function () {
    //https://mockio.cloud
    //http://localhost:9000
    const url = 'http://localhost:9000' + '/api/users/v1/public/signup';

    const unique = Date.now() + '-' + Math.floor(Math.random() * 100000);

    const payload = JSON.stringify({
        email: `test${unique}@example.com`,
        nickname: `tester${unique}`,
        password: 'Abcd1234!',
        recaptchaToken: 'dummy-token',
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
            "X-Load-Test-Bypass": "bG9hZFRlc3RCeUhlYWRlclRlc3Q=",
        },
    };

    const res = http.post(url, payload, params);

    const ok = check(res, {
        'status is 200 or 201': (r) => r.status === 200 || r.status === 201,
    });

    if (!ok) {
        console.log(`FAIL status=${res.status} body=${res.body}`);
    }
    sleep(1);
}