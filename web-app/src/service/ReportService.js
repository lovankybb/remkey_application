const BASE_URL = import.meta.env.VITE_API_BASE_URL;

async function getAllReports() {
  const url = `${BASE_URL}/api/v1/remkey/reports/cards`;
  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
    });

    return await response.json();
  } catch (err) {
    console.log(err);
  }
}

async function createCardReport(cardId, message, moreDesc) {
  const url = `${BASE_URL}/api/v1/remkey/reports/cards`;
  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
      body: JSON.stringify({
        cardId,
        message,
        moreDesc,
      }),
    });

    return await response.json();
  } catch (err) {
    console.log(err);
  }
}

async function deleteCardReport(cardReportId) {
  const url = `${BASE_URL}/api/v1/remkey/reports/cards/${cardReportId}`;
  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const response = await fetch(url, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
    });

    return await response.json();
  } catch (err) {
    console.log(err);
  }
}


async function acceptHandlingCardReport(cardReportId) {
  const url = `${BASE_URL}/api/v1/remkey/reports/cards/${cardReportId}`;
  const jwtToken = localStorage.getItem("jwtToken");

  try {
    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
    });

    return await response.json();
  } catch (err) {
    console.log(err);
  }
}


export { getAllReports, createCardReport, deleteCardReport, acceptHandlingCardReport };
