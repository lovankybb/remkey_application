const BASE_URL = import.meta.env.VITE_API_BASE_URL;

async function sendNotificationTokenToServer(token) {
  const url = `${BASE_URL}/api/v1/remkey/fire-base`;
  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
      body: JSON.stringify({
       token
      }),
    });

    return await response.json();
  } catch (err) {
    console.log(err);
  }
}


export {sendNotificationTokenToServer}; 