import successImg from "../../assets/success.gif";
import Button from "../Button/Button";

export default function SuccessPopup() {
  return (
    <div className="success-popup">
      <img src={successImg} alt="Success" />
    </div>
  );
}
