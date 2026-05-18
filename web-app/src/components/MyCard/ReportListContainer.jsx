import { useEffect, useState } from "react";
import { getAllReports } from "../../service/ReportService";
import DisableLayer from "../Popup/DisableLayer";
import ReportDetail from "./ReportDetail";

export default function ReportListContainer() {
  const [loading, setLoading] = useState(true);
  const [enableDisableLayer, setEnableDisableLayer] = useState(false);
  const [enableReportDetail, setEnableReportDetail] = useState(false);

  const [reports, setReports] = useState([]);
  const [selectedReport, setSelectedReport] = useState(null);


  const closeReportDetail = () => {
    setEnableDisableLayer(false);
    setEnableReportDetail(false);
  };

  const openReportDetail = (report) => {
    setSelectedReport(report);
    setEnableDisableLayer(true);
    setEnableReportDetail(true);
  };
  useEffect(() => {
    const fetchReports = async () => {
      try {
        const data = await getAllReports();
        if (data.code === 1000) {
          setReports(data.body);
        }
      } catch (err) {
        console.log(err);
      } finally {
        setLoading(false);
      }
    };
    fetchReports();
  }, []);

  if (loading) return <div className="report-list">Loading...</div>;
  return (
    <>
      <div className="report-list">
        {reports.map((report) => {
          return (
            <div className="report-list-item" onClick={()=> openReportDetail(report)}>
              <h3>{report.id}</h3>
              <p>{report.message}</p>
            </div>
          );
        })}
      </div>
      {enableDisableLayer && <DisableLayer />}
      {enableReportDetail && (
        <ReportDetail
          setReports={setReports}
          {...selectedReport}
          closeReportDetail={closeReportDetail}
        />
      )}
    </>
  );
}
