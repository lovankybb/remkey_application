import ExitHeader from "../Header/ExitHeader";

function HelpCenterTab({ exitBtn, isClose }) {
  return (
    <div
      className={`helpcenter-container ${isClose ? "popup-out" : "popup-in"}`}
    >
      <ExitHeader onClick={exitBtn} title="Trung tâm trợ giúp" />
      <div className="helpcenter-content">
        <h2>Tính năng đang phát triển</h2>
      </div>
    </div>
  );
}

export default HelpCenterTab;
