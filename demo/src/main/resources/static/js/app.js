// app.js
import { initBoxoffice } from './sections/boxoffice.js';
import { initCinema } from './sections/cinema.js';
// import { initMap } from './sections/showtime.js';

document.addEventListener('DOMContentLoaded', () => {
  const boxofficeSection = document.querySelector('#boxoffice');
  const cinemaSection = document.querySelector('#cinema');
  const mapSection = document.querySelector('#map');

  const sections = [boxofficeSection, cinemaSection, mapSection];
  const navLinks = document.querySelectorAll('header nav a');

  // ✅ 처음엔 박스오피스만 표시
  sections.forEach(sec => (sec.style.display = 'none'));
  boxofficeSection.style.display = 'block';
  initBoxoffice();

  // ✅ nav 클릭 시 섹션 전환
  navLinks.forEach(link => {
    link.addEventListener('click', e => {
      e.preventDefault(); // 페이지 점프 방지
      const targetId = link.getAttribute('href').replace('#', ''); // ex) 'cinema'
      const targetSection = document.getElementById(targetId);

      window.scrollTo({ top: 0, behavior: 'smooth' });

      // 모든 섹션 숨기기
      sections.forEach(sec => (sec.style.display = 'none'));

      // 선택한 섹션만 표시
      targetSection.style.display = 'block';

      // 필요 시 각 섹션 초기화
      if (targetId === 'boxoffice') initBoxoffice();
      if (targetId === 'cinema') initCinema();
      // if (targetId === 'map') initMap();
    });
  });
});
