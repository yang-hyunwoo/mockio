import http from 'k6/http';
import { check,sleep } from 'k6';

const users = Array.from({ length: 50 }, (_, i) => ({
    email: `tester${i + 1}@example.com`,
    password: 'Abcd1234!',
}));

export const options = {
    vus: 100,
    duration: '30s'
};

//http://localhost:9000
//https://mockio.cloud

const url = 'https://mockio.cloud';


export default function () {
    const user = users[(__VU - 1 + __ITER) % users.length];

    const payload = JSON.stringify({
        email: user.email,
        password: user.password,
    });

    const res = http.post(`${url}/api/auth/v1/login`, payload, {
        headers: {
            'Content-Type': 'application/json',
        },
    });

    const ok = check(res, {
        'status is 200 or 201': (r) => r.status === 200 || r.status === 201,
    });

    if (!ok) {
        console.log(`FAIL n=${n} status=${res.status} body=${res.body}`);
    }
    sleep(1);
}