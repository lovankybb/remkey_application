import { useState } from "react";
import SmallExitHeader from "../Header/SmallExitHeader";
import { createCardReport } from "../../service/ReportService";
import SuccessPopup from "../Popup/SuccessPopup";
import ErrorPopup from "../Popup/ErrorPopup";

const ReportContainer = ({ cardId, closeReportPopup }) => {
  const [selectReport, setSelectReport] = useState(4);
  const [reportMessage, setReportMessage] = useState("Khác");
  const [moreDesc, setMoreDesc] = useState("");

  const [enableSuccessPopup, setEnableSuccessPopup] = useState(false);
  const [enableErrorPopup, setEnableErrorPopup] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const reportItems = [
    {
      id: 1,
      content: "Nội dung mang tính kích động, bạo lực.",
    },
    {
      id: 2,
      content: "Chứa nội dung xúc phạm cá nhân, tổ chức.",
    },
    {
      id: 3,
      content: "Chứa nội dung khiêu dâm, tình dục.",
    },
    {
      id: 4,
      content: "Khác",
    },
  ];

  const openSuccessPopup = () => {
    setEnableSuccessPopup(true);
    setTimeout(() => {
      setEnableSuccessPopup(false);
    }, 2600);
  };

  const openErrorPopup = (message) => {
    setErrorMessage(message);
    setEnableErrorPopup(true);
  };

  const closeErrorPopup = () => {
    setEnableErrorPopup(false);
  };

  const submitReport = async () => {
    const data = await createCardReport(cardId, reportMessage, moreDesc);
    if (data.code === 1000) {
      openSuccessPopup();
    } else {
      const message = `code: ${data.code}
                      message: ${data.message}`;
      openErrorPopup(message);
    }
  };

  const handleSelectReport = (id, content) => {
    setSelectReport(id);
    setReportMessage(content);
  };

  return (
    <>
      <div className="report-container">
        <SmallExitHeader onClick={closeReportPopup} />
        <div>
          {reportItems.map((item) => {
            return (
              <div onClick={() => handleSelectReport(item.id, item.content)}>
                <input type="radio" checked={selectReport === item.id} />
                <span> {item.content}</span>
              </div>
            );
          })}
          <textarea
            onClick={(e) => e.preventDefault()}
            onChange={(e) => {
              setMoreDesc(e.target.value);
            }}
            className="report-description"
            placeholder="Mô tả..."
          ></textarea>
        </div>
        <button onClick={submitReport} className="send-report-btn">
          Gửi
        </button>
      </div>
      {enableSuccessPopup && <SuccessPopup />}
      {enableErrorPopup && (
        <ErrorPopup
          title="Error"
          message={errorMessage}
          onClick={closeErrorPopup}
        />
      )}
    </>
  );
};

export default ReportContainer;
