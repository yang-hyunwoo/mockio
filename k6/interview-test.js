import http from 'k6/http';
import { check,sleep } from 'k6';

const users = Array.from({ length: 50 }, (_, i) => ({
    email: `tester${i + 1}@example.com`,
    password: 'Abcd1234!',
}));

export const options = {
    vus: 5,
    duration: "10s",
};

//http://localhost:9000
//https://mockio.cloud
const url = 'https://mockio.cloud';


export function setup() {
    const tokens = users.map((user) => {
        const res = http.post(
            `${url}/api/auth/v1/login`,
            JSON.stringify({
                email: user.email,
                password: user.password,
            }),
            {
                headers: {
                    'Content-Type': 'application/json',
                },
            }
        );

        if (!(res.status === 200 || res.status === 201)) {
            return {
                email: user.email,
                accessToken: null,
            };
        }

        const accessToken = res.json('data.accessToken');

        return {
            email: user.email,
            accessToken,
        };
    });

    return { tokens };
}

export default function (data) {
    const tokenInfo = data.tokens[(__VU + __ITER) % data.tokens.length];
    const idempotencyKey = crypto.randomUUID();

    const payload = JSON.stringify({
        idempotencyKey: idempotencyKey,
    });
    if (!tokenInfo.accessToken) {
        return;
    }

    const res = http.post(
        `${url}/api/interview/v1/interviews/start-interview`,
        payload,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${tokenInfo.accessToken}`,
            },
        }
    );

    const ok = check(res, {
        'status is 200 or 201': (r) => r.status === 200 || r.status === 201,
    });

    if (!ok) {
        console.log(`FAIL email=${tokenInfo.email} status=${res.status} body=${res.body}`);
    }
    sleep(1);
}