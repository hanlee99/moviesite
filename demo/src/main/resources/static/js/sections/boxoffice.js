export function initBoxoffice() {
  const track = document.getElementById("movieTrack");
  const prevBtn = document.getElementById("prevBtn");
  const nextBtn = document.getElementById("nextBtn");
  const cards = Array.from(track.querySelectorAll(".card"));

  if (cards.length < 4) return; // 최소 4개 필요

  const slides = [
    [0, 1, 2, 3],   // 1~4
    [4, 5, 6, 7],   // 5~8
    [6, 7, 8, 9],   // 7~10
  ];

  let currentSlide = 0;
  let isTransitioning = false;

  track.style.display = "flex";
  track.style.gap = "16px";
  track.style.transition = "transform 0.5s ease-in-out";

  const updateSlide = (animate = true) => {
    const cardWidth = cards[0].getBoundingClientRect().width;
    const gap = 16;
    const firstIndex = slides[currentSlide][0];
    const offset = firstIndex * (cardWidth + gap);

    track.style.transition = animate ? "transform 0.5s ease-in-out" : "none";
    track.style.transform = `translateX(-${offset}px)`;
  };

  const moveSlide = (direction) => {
    if (isTransitioning) return;
    isTransitioning = true;

    // 0 → 1 → 2 → 0 순환 (또는 반대로)
    currentSlide = (currentSlide + direction + slides.length) % slides.length;

    updateSlide(true);
    setTimeout(() => (isTransitioning = false), 500);
  };

  nextBtn?.addEventListener("click", () => moveSlide(1));
  prevBtn?.addEventListener("click", () => moveSlide(-1));

  updateSlide(false);

}
