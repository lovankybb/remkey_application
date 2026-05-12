import chevronLeft from "../../assets/chevron-left-solid-full.svg";

export default function ExitHeader({ onClick, title }) {
  return (
    <>
      <div className="exit-header">
        <img src={chevronLeft} onClick={onClick} />
        <p>{title}</p>
      </div>
      <hr />
    </>
  );
}
