import plusIcon from "../../assets/plus-solid-full.svg";

function CardFeatureBar({ openPlusTab, setSearchTerm }) {
  function updateSearchTermValue() {
    const newSearchVal = document.querySelector("#filter-bar").value;
    setSearchTerm(newSearchVal.trim());
  }

  return (
    <>
      <div className="card-feature-bar">
        <img
          src={plusIcon}
          alt="Thêm"
          onClick={openPlusTab}
          className="selected"
        />
        <input
          type="text"
          id="filter-bar"
          placeholder="Tìm kiếm"
          onChange={updateSearchTermValue}
        />
      </div>
    </>
  );
}

export default CardFeatureBar;
