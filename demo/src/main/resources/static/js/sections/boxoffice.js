export function initBoxoffice() {
  const track = document.getElementById("movieTrack");
  const prevBtn = document.getElementById("prevBtn");
  const nextBtn = document.getElementById("nextBtn");
  const originalCards = Array.from(track.querySelectorAll(".card"));

  if (originalCards.length === 0) return;

  const perPage = 4;
  let currentIndex = perPage; // 복제된 앞부분 건너뛰고 시작
  let isTransitioning = false;

  // 🎬 무한 슬라이더를 위한 카드 복제
  // 마지막 4개를 앞에 복제
  for (let i = originalCards.length - perPage; i < originalCards.length; i++) {
    const clone = originalCards[i].cloneNode(true);
    track.insertBefore(clone, track.firstChild);
  }

  // 처음 4개를 뒤에 복제
  for (let i = 0; i < perPage; i++) {
    const clone = originalCards[i].cloneNode(true);
    track.appendChild(clone);
  }

  track.querySelectorAll("img").forEach(img => {
        const temp = new Image();
        temp.src = img.src;
  });

  const allCards = track.querySelectorAll(".card");

  // 스타일 설정
  track.style.display = "flex";
  track.style.gap = "16px";
  track.style.transition = "transform 0.5s ease-in-out";

  // 슬라이드 업데이트
  const updateSlide = (animate = true) => {
    const cardWidth = allCards[0].getBoundingClientRect().width;
    const gapWidth = 16;
    const offset = currentIndex * (cardWidth + gapWidth);

    if (!animate) track.style.transition = "none";
    else track.style.transition = "transform 0.5s ease-in-out";

    track.style.transform = `translateX(-${offset}px)`;
  };

  // ▶ 다음 버튼 (오른쪽으로 계속)
  nextBtn?.addEventListener("click", () => {
    if (isTransitioning) return;
    isTransitioning = true;

    currentIndex+=perPage;
    updateSlide(true);

    // 복제된 끝에 도달하면 실제 처음으로 순간이동
    setTimeout(() => {
      if (currentIndex >= originalCards.length + perPage) {
        currentIndex = perPage;
        updateSlide(false);
      }
      isTransitioning = false;
    }, 500);
  });


  prevBtn?.addEventListener("click", () => {
    if (isTransitioning) return;
    isTransitioning = true;

    // 현재가 첫 페이지면
    if (currentIndex === perPage) {
      // 1. 맨 끝 복제본으로 순간이동 (애니메이션 X)
      currentIndex = perPage + originalCards.length; // 끝 복제본
      updateSlide(false);

      // 2. 다음 프레임에서 왼쪽으로 애니메이션
      requestAnimationFrame(() => { // ← 이거로 바꾸기!
        currentIndex -= perPage;
        updateSlide(true);
        setTimeout(() => {
          isTransitioning = false;
        }, 500);
      });
    } else {
      // 일반 이동
      currentIndex -= perPage;
      updateSlide(true);

      setTimeout(() => {
        if (currentIndex < perPage) {
          const totalPages = Math.floor(originalCards.length / perPage);
          currentIndex = perPage * totalPages;
          updateSlide(false);
        }
        isTransitioning = false;
      }, 500);
    }
  });

  // 초기 위치 설정
  updateSlide(false);
}
