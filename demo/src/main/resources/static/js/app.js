// app.js
import { initBoxoffice } from './sections/boxoffice.js';
import { initCinema } from './sections/cinema.js';
// import { initMap } from './sections/map.js';

document.addEventListener('DOMContentLoaded', () => {
  const sections = {
    boxoffice: document.querySelector('#boxoffice'),
    cinema: document.querySelector('#cinema'),
    map: document.querySelector('#map')
  };

  const initializers = {
    boxoffice: initBoxoffice,
    cinema: initCinema,
    // map: initMap
  };

  // ✅ 초기 상태: 박스오피스만 표시
  Object.values(sections).forEach(sec => sec.style.display = 'none');
  sections.boxoffice.style.display = 'block';
  initBoxoffice();

  // ✅ 네비게이션 클릭 핸들러
  document.querySelectorAll('header nav a').forEach(link => {
    link.addEventListener('click', e => {
      e.preventDefault();

      const targetId = link.getAttribute('href').slice(1); // '#boxoffice' → 'boxoffice'
      const targetSection = sections[targetId];

      if (!targetSection) return;

      // 스크롤 최상단 이동
      window.scrollTo({ top: 0, behavior: 'smooth' });

      // 모든 섹션 숨기기
      Object.values(sections).forEach(sec => sec.style.display = 'none');

      // 선택 섹션 표시 및 초기화
      targetSection.style.display = 'block';
      initializers[targetId]?.();
    });
  });
});