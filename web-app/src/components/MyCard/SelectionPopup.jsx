import { useMemo, useState } from "react";
import xSolid from "../../assets/x-solid-full.svg";

function SelectionHeader({ onClick, setSearchTerm }) {
  function handleSetSearchTerm() {
    const searchVal = document.querySelector("#search-term").value;
    setSearchTerm(searchVal.trim());
  }

  return (
    <>
      <div className="selection-header">
        <input
          id="search-term"
          placeholder="Tìm kiếm"
          onChange={handleSetSearchTerm}
        />
        <img src={xSolid} onClick={onClick} />
      </div>
      <hr />
    </>
  );
}

function SelectionItem({ type, id, name, setValueFor }) {
  function handleSetValueFor() {
    setValueFor(id, name);
  }

  return (
    <p
      className="selection-item"
      id={`${type}_${id}`}
      onClick={handleSetValueFor}
    >
      {name}
    </p>
  );
}

export default function SelectionPopup({
  type,
  isClose,
  objects,
  setValueFor,
  closeSelectionTab,
}) {
  const [searchTerm, setSearchTerm] = useState("");

  const filteredList = useMemo(() => {
    if (searchTerm === "" || searchTerm === null) {
      return objects;
    }

    return objects.filter((item) =>
      item.name.toLowerCase().includes(searchTerm.toLocaleLowerCase()),
    );
  }, [objects, searchTerm]);

  return (
    <div className={`selection-popup ${isClose ? "popup-out" : "popup-in"}`}>
      <SelectionHeader
        onClick={closeSelectionTab}
        setSearchTerm={setSearchTerm}
      />
      <div className="selection-container">
        {filteredList.map((item) => {
          return (
            <SelectionItem
              key={item.id}
              type={type}
              id={item.id}
              setValueFor={setValueFor}
              name={item.name}
            />
          );
        })}
      </div>
    </div>
  );
}
