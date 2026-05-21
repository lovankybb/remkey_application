const BASE_URL = import.meta.env.VITE_API_BASE_URL;

async function getMyInfo() {
  const jwtToken = localStorage.getItem("jwtToken");

  const url = `${BASE_URL}/api/v1/remkey/users/my-info`;

  try {
    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
    });

    if (response.ok) return await response.json();
  } catch (err) {
    console.log(err);
    return null;
  }
}

async function updateUser() {}

async function logout() {
  const url = `${BASE_URL}/api/v1/remkey/auth/logout`;
  const jwtToken = localStorage.getItem("jwtToken");

  const cleanAuth = () => {
    localStorage.removeItem("jwtToken");
    localStorage.removeItem("roles");
  };
  try {
     await fetch(url, {
      method: "POST",
      headers: {
        "Content-type": "application/json",
      },
      body: JSON.stringify({
        token: jwtToken,
      }),
    });

    cleanAuth();
  } catch (err) {
    console.log(err);
  }
}

async function changePassword(currentPassword, newPassword, confirmPassword) {
  const userResponse = await getMyInfo();

  const userId = userResponse.body.id;

  const url = `${BASE_URL}/api/v1/remkey/users/${userId}/change-password`;
  const jwtToken = localStorage.getItem("jwtToken");

  const changePasswordRequest = {
    currPassword: currentPassword,
    newPassword: newPassword,
    confirmPassword: confirmPassword,
  };

  try {
    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
      body: JSON.stringify(changePasswordRequest),
    });

    return await response.json();
  } catch (err) {
    console.log(err);
  }
}

function checkRole() {
  const roles = localStorage.getItem("roles");

  if (roles.includes("ADMIN")) return "ADMIN";
  return "USER";
}

export { getMyInfo, updateUser, logout, changePassword, checkRole };
