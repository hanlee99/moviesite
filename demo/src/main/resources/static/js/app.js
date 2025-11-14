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

  // ⭐ init 함수는 최초 1회만 실행
  initBoxoffice();
  initCinema();
  // initMap();

  // ⭐ 처음엔 박스오피스만 표시
  sections.forEach(sec => (sec.style.display = 'none'));
  boxofficeSection.style.display = 'block';

  // ⭐ nav 클릭 시 섹션 전환만 수행
  navLinks.forEach(link => {
    link.addEventListener('click', e => {
      e.preventDefault();

      const targetId = link.getAttribute('href').replace('#', '');
      const targetSection = document.getElementById(targetId);

      window.scrollTo({ top: 0, behavior: 'smooth' });

      // 모든 섹션 숨기기
      sections.forEach(sec => (sec.style.display = 'none'));

      // 선택한 섹션만 표시
      targetSection.style.display = 'block';
    });
  });
});
