const BASE_URL = import.meta.env.VITE_API_BASE_URL;
import { useEffect, useState } from "react";
import defaultImage from "../../assets/image-regular-full.svg";
import { getCardById } from "../../service/CardSevice";
import SmallExitHeader from "../Header/SmallExitHeader";
import {
  deleteCardReport,
  acceptHandlingCardReport,
} from "../../service/ReportService";
import SuccessPopup from "../Popup/SuccessPopup";
import ErrorPopup from "../Popup/ErrorPopup";

export default function ReportDetail({
  id,
  cardId,
  reportMessage,
  moreDesc,
  closeReportDetail,
  setReports
}) {
  const [loading, setLoading] = useState(true);
  const [card, setCard] = useState(null);
  const [enableSuccessPopup, setEnableSuccessPopup] = useState(false);
  const [enableErrorPopup, setEnableErrorPopup] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  useEffect(() => {
    const fetchCard = async () => {
      try {
        const data = await getCardById(cardId);
        if (data.code === 1000) {
          setCard(data.body);
        }
      } catch (err) {
        console.log(err);
      } finally {
        setLoading(false);
      }
    };

    fetchCard();
  }, []);

  const openSuccessPopup = () => {
    setEnableSuccessPopup(true);
    setTimeout(() => {
      setEnableSuccessPopup(false);
      closeReportDetail();
    }, 2000);
  };

  const openErrorPopup = (message) => {
    setErrorMessage(message);
    setEnableErrorPopup(true);
  };

  const closeErrorPopup = () => {
    setEnableErrorPopup(false);
  };

  const handleAcceptAndRemoveCardReport = async () => {
    const data = await acceptHandlingCardReport(id);
    if (data.code === 1000) {
      setReports(prev => prev.filter(report => report.id !== id));
      openSuccessPopup();
    } else {
      const message = `code: ${data.code}
                      message: ${data.message}`;
      openErrorPopup(message);
    }
  };

  const rejectCardReport = async () => {
    const data = await deleteCardReport(id);
    if (data.code === 1000) {
      openSuccessPopup();
      setReports(prev => prev.filter(report => report.id !== id));
    } else {
      const message = `code: ${data.code}
                      message: ${data.message}`;
      openErrorPopup(message);
    }
  };

  if (loading)
    return (
      <div className="report-detail-container slide-fade-in">
        <SmallExitHeader onClick={closeReportDetail} />
        <p>
          <strong>Report id:</strong>
          {id}
        </p>
        <p>
          <strong>Report message:</strong>
          {reportMessage}
        </p>
        <p>
          <strong>Report description:</strong>
          {moreDesc}
        </p>
        Loading ...
      </div>
    );

  if (!loading && !card)
    return (
      <div className="report-detail-container">
        <SmallExitHeader onClick={closeReportDetail} />
        <div className="report-detail-content">
          <section>
            <p>
              <strong>Report id:</strong>
              {id}
            </p>
            <br />
            <p>
              <strong>Report message:</strong>
              {reportMessage}
            </p>
            <br />
            <p>
              <strong>Report description:</strong>
              {moreDesc}
            </p>
          </section>
          <br />
          <p>Loading card FAILED ...!</p>
        </div>
      </div>
    );
  return (
    <>
      <div className="report-detail-container">
        <SmallExitHeader onClick={closeReportDetail} />
        <div className="report-detail-content">
          <section>
            <p>
              <strong>Report id:</strong>
              {card.id}
            </p>
            <br />
            <p>
              <strong>Report message:</strong>
              {reportMessage}
            </p>
            <br />
            <p>
              <strong>Report description:</strong>
              {moreDesc}
            </p>
          </section>
          <div className="card-in-report">
            <img src={defaultImage} />
            <h3>{card.question}</h3>
            <br />
            <p>{card.answer}</p>
            <br />
            <p>
              <strong>Language: </strong>
              {card.language}
            </p>
            <br />
            <p>
              <strong>Topic: </strong>
              {card.topic}
            </p>
          </div>
        </div>
        <div className="handle-report-btn-container">
          <button className="reject-report-btn" onClick={rejectCardReport}>
            Reject
          </button>
          <button
            className="accept-report-btn"
            onClick={handleAcceptAndRemoveCardReport}
          >
            Accept and remove card
          </button>
        </div>
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
}
