import ExitHeader from "../Header/ExitHeader";
import ReportListContainer from "./ReportListContainer";

const ReportManagement = ({ isClose, exitBtn }) => {

  return (
    <>
      <div
        className={`new-tab-container ${isClose ? "popup-out" : "popup-in"}`}
      >
        <ExitHeader onClick={exitBtn} title="Report management" />
        <ReportListContainer/>
      </div>
    </>
  );
};

export default ReportManagement;


