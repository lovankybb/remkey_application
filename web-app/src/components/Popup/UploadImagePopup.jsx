import Button from "../Button/Button";
import SmallExitHeader from "../Header/SmallExitHeader";

export default function UploadImagePopup({ confirm, isClose, closeTab }) {
  return (
    <div className={`uploadimage-popup ${isClose ? 'popup-out': 'popup-in'}`}>
      <SmallExitHeader onClick={closeTab}/>
      <div className="uploadimage-popup-container">
        <input type="file" id="image" />
        <Button type="blue-btn" title="Xác nhận"  onClick={confirm}/>
      </div>
    </div>
  );
}
