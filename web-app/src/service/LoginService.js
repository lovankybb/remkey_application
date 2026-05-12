import { getMyInfo } from "./UserService";
const BASE_URL = import.meta.env.VITE_API_BASE_URL;

export default async function login(username, password) {
  const url = `${BASE_URL}/api/v1/remkey/auth`;
  try {
    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        username: username.trim(),
        password: password.trim(),
      }),
    });

    const data = await response.json();

    if (data.code !== 1000) return data;

    localStorage.setItem("jwtToken", data.body.token);

    const myInfoResponse = await getMyInfo();

    const roles = myInfoResponse.body.roles;

    localStorage.setItem("roles", roles);
  } catch (err) {
    console.log(err);
  }
  return null;
}
