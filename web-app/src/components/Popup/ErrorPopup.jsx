import Button from "../Button/Button";

export default function ErrorPopup({ title, message, onClick }) {
  return (
    <div className="err-popup">
      <h1>{title}</h1>
      <p>{message}</p>
      <Button title="Cancel" type="exit-popup-btn" onClick={onClick} />
    </div>
  );
}
