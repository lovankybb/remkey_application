function RatingBtns({ moveToNextCard, updateNextReviewTime }) {
  function handleUpdateStudyTime(event) {
    const rating = event.target.value;
    updateNextReviewTime(rating);
    moveToNextCard();
  }

  return (
    <div className="rate-btn-container">
      <button
        value="4"
        className="rate-btn"
        onClick={handleUpdateStudyTime}
        id="easy-btn"
      >
        Dễ
      </button>
      <button
        value="3"
        className="rate-btn"
        onClick={handleUpdateStudyTime}
        id="good-btn"
      >
        Bình thường
      </button>
      <button
        value="2"
        className="rate-btn"
        onClick={handleUpdateStudyTime}
        id="hard-btn"
      >
        Khó
      </button>
      <button
        value="1"
        className="rate-btn"
        onClick={handleUpdateStudyTime}
        id="again-btn"
      >
        Quên
      </button>
    </div>
  );
}

export default RatingBtns;
