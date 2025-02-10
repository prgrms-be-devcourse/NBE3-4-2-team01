'use client';

import { useEffect, useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import styles from './Navigation.module.css';

interface UserState {
  isLoggedIn: boolean;
  userType: 'USER' | 'BUSINESS' | 'ADMIN' | 'ANONYMOUS' | null;
}

// 쿠키 가져오는 유틸리티 함수
const getTokenFromCookie = (): string | null => {
  if (typeof window === 'undefined') return null;
  
  const cookies = document.cookie.split("; ");
  const role = cookies.find((cookie) => cookie.startsWith("role="));
  
  console.log(role);

  return role ? role.split("=")[1] : null;
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
      
      if (!token) {
        setUser({
          isLoggedIn: false,
          userType: 'ANONYMOUS'
        });
      } else if(token === 'USER') {
        setUser({
          isLoggedIn: true,
          userType: 'USER'
        });
      } else if(token === 'BUSINESS') {
        setUser({
          isLoggedIn: true,
          userType: 'BUSINESS'
        });
      } else if(token === 'ADMIN') {
        setUser({
          isLoggedIn: true,
          userType: 'ADMIN'
        });
      }
    };

    checkLoginStatus();
  }, []);

  const handleLogout = async () => {
    setUser({
      isLoggedIn: false,
      userType: 'ANONYMOUS'
    });
    // 백엔드에 로그아웃 api 요청
  };

  return (
    <nav className={styles.navigation}>
      <div className={styles.container}>
        <Link href={user?.userType === 'ADMIN' ? '/admin' : '/'} className={styles.logo}>
          서울호텔
        </Link>
        
        <div className={styles.links}>
          {/* ANONYMOUS 상태 */}
          {!user.isLoggedIn ? (
            <>
              <Link href="/login" className={styles.link}>
                로그인
              </Link>
            </>
          ) : (
            <>
              {/* USER 상태 */}
              {user.userType === 'USER' && (
                <>
                  <Link href="/business/register" className={styles.link}>
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
                </>
              )}

              {/* BUSINESS 상태 */}
              {user.userType === 'BUSINESS' && (
                <>
                  <Link href="/business/rooms" className={styles.link}>
                    객실 목록
                  </Link>
                  <Link href="/business/hotel" className={styles.link}>
                    내 호텔
                  </Link>
                </>
              )}

              {/* ADMIN 상태 */}
              {user.userType === 'ADMIN' && (
                <>
                  <Link href="/admin/business" className={styles.link}>
                    사업자 관리
                  </Link>
                  <Link href="/admin/hotels" className={styles.link}>
                    호텔 관리
                  </Link>
                  <Link href="/admin/hotel-options" className={styles.link}>
                    호텔 옵션 관리
                  </Link>
                  <Link href="/admin/room-options" className={styles.link}>
                    객실 옵션 관리
                  </Link>
                </>
              )}

              {/* 로그아웃 버튼 */}
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
