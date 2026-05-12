const BASE_URL = import.meta.env.VITE_API_BASE_URL;

async function verifyUserCreation(email, code) {
  const url = `${BASE_URL}/api/v1/remkey/users/create/verify`;
  const respose = await fetch(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      email: email,
      code: code,
    }),
  });

  return await respose.json();
}

export default verifyUserCreation;
