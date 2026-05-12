import Button from "../Button/Button";

export default function ConfirmPopup({ message, cancel, confirm }) {
  return (
    <div className="confirm-popup slide-fade-in">
      <h4>{message}</h4>
      <div>
        <Button type="confirm-btn" title="Xác nhân" onClick={confirm} />
        <Button type="cancel-btn" title="Hủy" onClick={cancel} />
      </div>
    </div>
  );
}
