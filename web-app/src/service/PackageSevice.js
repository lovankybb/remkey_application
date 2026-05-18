const BASE_URL = import.meta.env.VITE_API_BASE_URL;

const getAllPackages = async () => {
  const url = `${BASE_URL}/api/v1/remkey/packages`;
  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const resp = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
    });
    return await resp.json();
  } catch (error) {
    console.log(error);
  }
};

const createPackage = async (name, price, quota, duration, description) => {
  const url = `${BASE_URL}/api/v1/remkey/packages`;
  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const resp = await fetch(url, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${jwtToken}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        name,
        price,
        quota,
        duration,
        description,
      }),
    });
    return await resp.json();
  } catch (error) {
    console.log(error);
  }
};

const deletePackage = async (packageId) => {
  const url = `${BASE_URL}/api/v1/remkey/packages/${packageId}`;
  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const resp = await fetch(url, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${jwtToken}`,
        "Content-Type": "application/json",
      },
    });
    return await resp.json();
  } catch (error) {
    console.log(error);
  }
};

export { getAllPackages, createPackage, deletePackage };
