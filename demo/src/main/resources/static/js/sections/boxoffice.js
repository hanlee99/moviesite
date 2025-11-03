export function initBoxoffice() {
  const track = document.getElementById("movieTrack");
  const prevBtn = document.getElementById("prevBtn");
  const nextBtn = document.getElementById("nextBtn");
  const title = document.getElementById("title");
  const showRange = document.getElementById("showRange");
  const slideViewBtn = document.getElementById("slideViewBtn");
  const listViewBtn = document.getElementById("listViewBtn");
  const toggleBtn = document.getElementById("toggleViewBtn");

  let isListView = false;
  const { daily, weekly, nowPlaying, upcoming } = window.boxofficeData;
  let currentSlide = 0;
  let slides = [];
  let movies = [];
  let len = 4;
  let currentType = "daily"; // "upcoming"도 가능

  track.style.display = "flex";
  track.style.gap = "16px";
  track.style.transition = "transform 0.5s ease";

  // 슬라이드 그룹 만들기 (마지막은 4개로)
  function makeSlides(total) {
    const result = [];
    const lastStart = Math.max(0, total - len);

    for (let i = 0; i < lastStart; i += len) {
      result.push([i, i+1, i+2, i+3]);
    }

    if (total > len) {
      const last = [];
      for (let i = lastStart; i < total; i++) last.push(i);
      result.push(last);
    } else if (total <= len) {
      const all = [];
      for (let i = 0; i < total; i++) all.push(i);
      result.push(all);
    }

    return result;
  }

  function renderCards() {
    // 기존 카드 제거
    while (track.firstChild) track.removeChild(track.firstChild);

    movies.forEach((m, i) => {
      const card = document.createElement("div");
      card.className = "card";
      card.dataset.index = i;

      const figure = document.createElement("figure");
      const img = document.createElement("img");
      img.src = m.posterUrl || (m.posters?.split("|")[0]) || "/images/no-poster.png";
      img.alt = m.title;
      figure.appendChild(img);

      const body = document.createElement("div");
      body.className = "card-body p-4 text-center";

      const title = document.createElement("h2");
      title.className = "card-title text-base font-semibold justify-center";
      title.textContent = m.title;

      const date = document.createElement("p");
      date.className = "text-gray-500 text-sm";
      date.textContent = `${m.openDt || m.repRlsDate || ""} 개봉`;

      body.append(title, date);
      card.append(figure, body);

      track.appendChild(card);
    });

    setTimeout(() => updateSlide(false), 50);
  }


  // 슬라이드 이동
  function updateSlide(animate = true) {
    const cards = track.querySelectorAll(".card");
    if (!cards.length) return;

    const cardWidth = cards[0].offsetWidth;
    const offset = slides[currentSlide][0] * (cardWidth + 16);

    track.style.transition = animate ? "transform 0.5s ease" : "none";
    track.style.transform = `translateX(-${offset}px)`;
  }

  function move(dir) {
    currentSlide = (currentSlide + dir + slides.length) % slides.length;
    updateSlide(true);
  }

  // 카테고리 전환
  function switchTo(type, data, label, isList) {
    currentType=type;
    currentSlide = 0;
    movies = isList ? data : data.movies;
    slides = makeSlides(movies.length);

    title.textContent = label;
    showRange.textContent = (type=='daily' || type=='weekly') ? data.showRange :
        new Date().toISOString().slice(0, 10).replace(/-/g, "");

    prevBtn.style.display = movies.length > len ? "flex" : "none";
    nextBtn.style.display = movies.length > len ? "flex" : "none";

    renderCards();

    // ✅ 보기 모드에 따라 화살표 표시 제어
      if (isListView) {
        prevBtn.style.display = "none";
        nextBtn.style.display = "none";
      } else {
        prevBtn.style.display = movies.length > 4 ? "flex" : "none";
        nextBtn.style.display = movies.length > 4 ? "flex" : "none";
      }

    Object.values(btns).forEach(b => b?.classList.remove("bg-blue-50"));
    btns[type]?.classList.add("bg-blue-50");

    updateToggleBtn(type);
  }
  const btns = {
    daily: document.getElementById("btnDaily"),
    weekly: document.getElementById("btnWeekly"),
    now: document.getElementById("btnNow"),
    upcoming: document.getElementById("btnUpcoming"),
  };

  btns.daily?.addEventListener("click", () => switchTo("daily", daily, "일간 박스오피스", false));
  btns.weekly?.addEventListener("click", () => switchTo("weekly", weekly, "주간 박스오피스", false));
  btns.now?.addEventListener("click", () => switchTo("now", nowPlaying, "최근 개봉 영화", true));
  btns.upcoming?.addEventListener("click", () => switchTo("upcoming", upcoming, "상영 예정 영화", true));

  nextBtn?.addEventListener("click", () => move(1));
  prevBtn?.addEventListener("click", () => move(-1));

  // 초기화
  switchTo("daily", daily, "일간 박스오피스", false);

listViewBtn?.addEventListener("click", () => {
  isListView = true;
  track.style.transition = "none";
  track.style.transform = "translateX(0)";
  track.style.flexWrap = "wrap";
  track.style.justifyContent = "center";
  prevBtn.style.display = "none";
  nextBtn.style.display = "none";
  // 버튼 강조
  listViewBtn.classList.add("bg-blue-100");
  slideViewBtn.classList.remove("bg-blue-100");

  updateToggleBtn(currentType);
});

slideViewBtn?.addEventListener("click", () => {
  isListView = false;
  track.style.flexWrap = "nowrap";
  track.style.justifyContent = "flex-start";
  prevBtn.style.display = movies.length > len ? "flex" : "none";
  nextBtn.style.display = movies.length > len ? "flex" : "none";
  updateSlide(false);
  // 버튼 강조
  slideViewBtn.classList.add("bg-blue-100");
  listViewBtn.classList.remove("bg-blue-100");

  updateToggleBtn(currentType);
});

function updateToggleBtn(type) {
  if ((type === "now" || type === "upcoming") && isListView) {
    toggleBtn.classList.remove("hidden");
  } else {
    toggleBtn.classList.add("hidden");
  }
}
  slideViewBtn.classList.add("bg-blue-100");
  listViewBtn.classList.remove("bg-blue-100");

  let currentPage = 0;
  const pageSize = 20;
  const maxCount = 150;
  let totalLoaded = 0;

  toggleBtn?.addEventListener("click", async () => {
    // ✅ 더보기 버튼 누르면 다른 기능은 잠시 꺼두기
    prevBtn.disabled = true;
    nextBtn.disabled = true;
    slideViewBtn.disabled = true;
    listViewBtn.disabled = true;
    Object.values(btns).forEach(b => (b.disabled = true));

    if (totalLoaded >= maxCount) {
      toggleBtn.textContent = "마지막 페이지입니다";
      toggleBtn.disabled = true;
      return;
    }

    currentPage++;
    const endpoint = currentType === "now" ? "/api/now-playing" : "/api/upcoming";
    const url = `${endpoint}?page=${currentPage}&size=${pageSize}`;

    try {
      const res = await fetch(url);
      const newMovies = await res.json();

      if (!Array.isArray(newMovies) || newMovies.length === 0) {
        toggleBtn.textContent = "더 이상 데이터 없음";
        toggleBtn.disabled = true;
        return;
      }

      // ✅ 누적 개수 갱신
      totalLoaded += newMovies.length;

      // ✅ 새 카드 append
      newMovies.forEach((m) => {
        const card = document.createElement("div");
        card.className = "card";
        const figure = document.createElement("figure");
        const img = document.createElement("img");
        img.src = m.posterUrl || (m.posters?.split("|")[0]) || "/images/no-poster.png";
        img.alt = m.title;
        figure.appendChild(img);

        const body = document.createElement("div");
        body.className = "card-body p-4 text-center";

        const h2 = document.createElement("h2");
        h2.className = "card-title text-base font-semibold justify-center";
        h2.textContent = m.title;

        const date = document.createElement("p");
        date.className = "text-gray-500 text-sm";
        date.textContent = `${m.openDt || m.repRlsDate || ""} 개봉`;

        body.append(h2, date);
        card.append(figure, body);
        track.appendChild(card);
      });
    } catch (err) {
      console.error("더보기 요청 실패:", err);
      toggleBtn.classList.add("hidden");
    }
  });


}