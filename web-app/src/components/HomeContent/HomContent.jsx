import { useEffect, useRef, useState } from "react";
import { getNewestCards, getNextCardsFromId } from "../../service/CardSevice";
import OriginCard from "./OriginCard";

function HomeContent() {
  const [cards, setCards] = useState([]);
  const [loading, setLoading] = useState(false);
  const [hasNext, setHasNext] = useState(true);
  const [lastIdx, setLastIdx] = useState(null);

  const observerTarget = useRef(null);

  const fetchCards = async () => {
    if (!hasNext || loading) return;

    setLoading(true);

    try {
      const data = lastIdx
        ? await getNextCardsFromId(lastIdx)
        : await getNewestCards();
      if (data.code === 1000) {
        setCards((prev) => [...prev, ...data.body.content]);
        setHasNext(!data.body.last);
        if (data.body.size > 0) {
          setLastIdx(data.body.content[data.body.size - 1].id);
        }
      }
    } catch (err) {
      console.log(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && hasNext) {
          fetchCards();
        }
      },
      { threshold: 1.0 },
    );

    if (observerTarget.current) {
      observer.observe(observerTarget.current);
    }

    return () => observer.disconnect();
  }, [lastIdx, hasNext]);


  return (
    <>
      <div className="home-content-container">
        {cards.map((card) => {
          return <OriginCard key={card.id} {...card} />;
        })}

        <div ref={observerTarget} style={{ height: "20px" }}>
          {loading && <p>Đang tải....</p>}
          {!hasNext && <p>Bạn đã xem hết thẻ</p>}
        </div>
      </div>
    </>
  );
}

export default HomeContent;
