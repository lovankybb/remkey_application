import Button from "../Button/Button.jsx";

export default function OtpVerifyPopup({ cancel, submit, onChange }) {
  return (
    <div className="otp-verify-popup">
      <h3>Vui lòng kiểm tra email!</h3>
      <input type="text" onChange={onChange} />
      <div>
        <Button title="Cancel" type="exit-otp-verify-btn" onClick={cancel} />
        <Button title="Gửi" type="submit-otp-verify-btn" onClick={submit} />
      </div>
    </div>
  );
}
