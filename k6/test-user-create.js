import http from 'k6/http';
import { check } from 'k6';

export const options = {
    vus: 1,
    iterations: 100,
};
//https://mockio.cloud
//http://localhost:9000
const url = "https://mockio.cloud";

export default function () {
    const n = __ITER + 1;

    const payload = JSON.stringify({
        email: `tester${n}@example.com`,
        nickname: `tester${n}`,
        password: 'Abcd1234!',
        recaptchaToken: 'dummy-token',
    });

    const res = http.post(`${url}/api/users/v1/public/signup`, payload, {
        headers: {
            'Content-Type': 'application/json',
            "X-Load-Test-Bypass": "bG9hZFRlc3RCeUhlYWRlclRlc3Q=",
        },
    });

    const ok = check(res, {
        'status is 200 or 201': (r) => r.status === 200 || r.status === 201,
    });

    if (!ok) {
        console.log(`FAIL n=${n} status=${res.status} body=${res.body}`);
    }
}