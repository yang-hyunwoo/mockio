import http from 'k6/http';
import { check, sleep } from 'k6';
import { FormData } from 'https://jslib.k6.io/formdata/0.0.2/index.js';

const users = Array.from({ length: 50 }, (_, i) => ({
    email: `tester${i + 1}@example.com`,
    password: 'Abcd1234!',
}));

export const options = {
    vus: 50,
    duration: '30s',
};
const fakeImage = open('./dummy.png', 'b');
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

        return {
            email: user.email,
            accessToken: res.json('data.accessToken'),
        };
    });

    return { tokens };
}

export default function (data) {
    const tokenInfo = data.tokens[(__VU + __ITER) % data.tokens.length];

    if (!tokenInfo.accessToken) {
        return;
    }

    const formData = new FormData();
    formData.append('nickname', `suser-${__VU}-${__ITER}`);
   // formData.append('profileImage',http.file(fakeImage, 'dummy.png', 'image/png'));
    const res = http.patch(
        `${url}/api/users/v1/mypage/profile`,
        formData.body(),
        {
            headers: {
                Authorization: `Bearer ${tokenInfo.accessToken}`,
                'Content-Type': `multipart/form-data; boundary=${formData.boundary}`,
            },
        }
    );

    const ok = check(res, {
        'success status': (r) => [200, 201, 204].includes(r.status),
    });

    if (!ok) {
        console.log(`FAIL email=${tokenInfo.email} status=${res.status} body=${res.body}`);
    } else {
        console.log(`SUCCESS email=${tokenInfo.email} status=${res.status}`);
    }

    sleep(1);
}