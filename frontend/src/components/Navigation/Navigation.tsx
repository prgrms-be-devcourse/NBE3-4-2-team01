'use client';

import { useEffect, useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import styles from './Navigation.module.css';

interface UserState {
  isLoggedIn: boolean;
  userType: 'GUEST' | 'ANONYMOUS' | null;
}

// 쿠키 가져오는 유틸리티 함수
const getTokenFromCookie = (): string | null => {
  if (typeof window === 'undefined') return null;
  
    const cookies = document.cookie.split("; ");
    const accessToken = cookies.find((cookie) => cookie.startsWith("access_token="));
    console.log(accessToken);
    
    return accessToken ? accessToken.split("=")[1] : null;
};

export default function Navigation() {
  const router = useRouter();
  const [user, setUser] = useState<UserState>({
    isLoggedIn: false,
    userType: null
  });

  useEffect(() => {
    const checkLoginStatus = () => {
      const token = getTokenFromCookie();
      
      if (token) {
        // JWT 토큰이 있는 경우
        setUser({
          isLoggedIn: true,
          userType: 'GUEST'
        });
      } else {
        setUser({
          isLoggedIn: false,
          userType: 'ANONYMOUS'
        });
      }
    };

    checkLoginStatus();
  }, []);

  const handleLogout = async () => {
    // 쿠키 삭제 (보안을 위해 HttpOnly, Secure 옵션 추가)
    document.cookie = 'accessToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; secure; samesite=strict';
    
    setUser({
      isLoggedIn: false,
      userType: 'ANONYMOUS'
    });
    
    router.push('/login');
    router.refresh(); // 페이지 새로고침하여 상태 초기화
  };

  return (
    <nav className={styles.navigation}>
      <div className={styles.container}>
        <Link href="/" className={styles.logo}>
          서울호텔
        </Link>
        
        <div className={styles.links}>
          {!user.isLoggedIn ? (
            <Link href="/login" className={styles.link}>
              로그인
            </Link>
          ) : (
            <>
              <Link href="/business/regster" className={styles.link}>
                사업자 등록
              </Link>
              <Link href="/me/orders" className={styles.link}>
                내 예약
              </Link>
              <Link href="/me/reviews" className={styles.link}>
                내 리뷰
              </Link>
              <Link href="/me/favorites" className={styles.link}>
                즐겨찾기
              </Link>
              <button 
                onClick={handleLogout}
                className={styles.logoutButton}
              >
                로그아웃
              </button>
            </>
          )}
        </div>
      </div>
    </nav>
  );
}