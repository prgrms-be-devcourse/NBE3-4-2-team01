frontend
├─ .gitignore
├─ components.json
├─ eslint.config.mjs
├─ next.config.ts
├─ package-lock.json
├─ package.json
├─ postcss.config.mjs
├─ public
│  ├─ file.svg
│  ├─ globe.svg
│  ├─ next.svg
│  ├─ vercel.svg
│  └─ window.svg
├─ README.md
├─ src
│  ├─ app
│  │  ├─ admin
│  │  │  ├─ business
│  │  │  │  └─ page.tsx
│  │  │  ├─ hotel-options
│  │  │  │  └─ page.tsx
│  │  │  ├─ hotels
│  │  │  │  └─ page.tsx
│  │  │  └─ room-options
│  │  │     └─ page.tsx
│  │  ├─ business
│  │  │  ├─ bookings
│  │  │  │  └─ page.tsx
│  │  │  ├─ hotels
│  │  │  │  ├─ page.tsx
│  │  │  │  └─ [hotelId]
│  │  │  │     └─ reviews
│  │  │  │        └─ page.tsx
│  │  │  ├─ register
│  │  │  │  └─ page.tsx
│  │  │  ├─ revenue
│  │  │  │  └─ page.tsx
│  │  │  └─ rooms
│  │  │     └─ [roomId]
│  │  │        └─ page.tsx
│  │  ├─ favicon.ico
│  │  ├─ globals.css
│  │  ├─ hotels
│  │  │  ├─ page.tsx
│  │  │  └─ [hotelId]
│  │  │     ├─ page.tsx
│  │  │     └─ reviews
│  │  │        └─ page.tsx
│  │  ├─ join
│  │  │  └─ page.tsx
│  │  ├─ layout.tsx
│  │  ├─ login
│  │  │  └─ page.tsx
│  │  ├─ me
│  │  │  ├─ favorites
│  │  │  │  └─ page.tsx
│  │  │  ├─ orders
│  │  │  │  ├─ page.tsx
│  │  │  │  └─ [bookingId]
│  │  │  │     └─ page.tsx
│  │  │  └─ reviews
│  │  │     ├─ page.tsx
│  │  │     └─ [reviewId]
│  │  │        └─ page.tsx
│  │  ├─ orders
│  │  │  └─ payment
│  │  │     └─ page.tsx
│  │  └─ page.tsx
│  ├─ components
│  │  ├─ Pagination
│  │  │  ├─ Pagination.module.css
│  │  │  └─ Pagination.tsx
│  │  ├─ ReviewWithComment
│  │  │  ├─ HotelReviews.tsx
│  │  │  ├─ HotelReviewWithComment.tsx
│  │  │  ├─ MyReviews.tsx
│  │  │  ├─ MyReviewWithComment.tsx
│  │  │  └─ ReviewList.tsx
│  │  └─ ui
│  │     ├─ button.tsx
│  │     ├─ card.tsx
│  │     ├─ input.tsx
│  │     ├─ label.tsx
│  │     └─ textarea.tsx
│  └─ lib
│     ├─ api
│     │  ├─ AwsS3Api.ts
│     │  ├─ ReviewApi.ts
│     │  └─ ReviewCommentApi.ts
│     ├─ types
│     │  ├─ Empty.ts
│     │  ├─ GetReviewResponse.ts
│     │  ├─ HotelReviewListResponse.ts
│     │  ├─ HotelReviewResponse.ts
│     │  ├─ HotelReviewWithCommentDto.ts
│     │  ├─ MyReviewResponse.ts
│     │  ├─ MyReviewWithCommentDto.ts
│     │  ├─ PageDto.ts
│     │  ├─ PostReviewRequest.ts
│     │  ├─ PresignedUrlsResponse.ts
│     │  ├─ ReviewCommentDto.ts
│     │  ├─ ReviewDto.ts
│     │  ├─ RsData.ts
│     │  └─ UpdateReviewRequest.ts
│     └─ utils.ts
├─ tailwind.config.ts
└─ tsconfig.json
