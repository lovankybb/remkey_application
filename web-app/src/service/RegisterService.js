const BASE_URL = import.meta.env.VITE_API_BASE_URL;

export default async function register(username, password, email) {
  console.log(password + "======== run in register");
  const url = `${BASE_URL}/api/v1/remkey/users`;
  try {
    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        username: username,
        password: password,
        email: email,
      }),
    });

    return await response.json();
  } catch (err) {
    console.log(err);
  }
}
