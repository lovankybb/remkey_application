import { useState } from "react";
import menuIcon from "../../assets/bars-solid-full.svg";
import CardOption from "./CardOption";

export default function CardHeader({ openCardOptions }) {
  const [enableCardOption, setEnableCardOption] = useState(false);

  const openCardOption = () => {
    setEnableCardOption(true);
  };
  const closeCardOption = () => {
    setEnableCardOption(false);
  };

  return (
    <div className="card-header">
      <img onClick={openCardOption} src={menuIcon} alt="card-menu" />
      {enableCardOption && (
        <CardOption {...openCardOptions} exitBtn={closeCardOption} />
      )}
    </div>
  );
}
