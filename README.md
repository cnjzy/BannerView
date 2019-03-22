# BannerView
android banner view


Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://javadoc.jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.cnjzy:BannerView:Tag'
	}

Step 3. Add the BannerView in you layout

	<com.jzy.bannerview.BannerView
            android:id="@+id/bannerView"
            android:layout_width="match_parent"
            android:layout_height="200dp" />

Step 4. in Activity

	public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_POSITION = "extra_positionF";

    private BannerView.OnBannerItemClickListener mOnBannerItemClickListener = new BannerView.OnBannerItemClickListener() {
        @Override
        public void onBannerItemClick(View view, int position) {
            Toast.makeText(SampleBannerViewActivity.this, "click:" + position, Toast.LENGTH_SHORT).show();
        }
    };

    private List<String> mImageList = new ArrayList<>();

    {
        mImageList.add("http://img3.imgtn.bdimg.com/it/u=1117144272,3782506471&fm=26&gp=0.jpg");
        mImageList.add("http://img4.imgtn.bdimg.com/it/u=3409415147,2909420565&fm=26&gp=0.jpg");
        mImageList.add("http://img1.imgtn.bdimg.com/it/u=1444799040,2563782889&fm=11&gp=0.jpg");
        mImageList.add("http://img1.imgtn.bdimg.com/it/u=1528790140,3963439459&fm=26&gp=0.jpg");
        mImageList.add("http://g.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=2ba66742a686c91708565a3dfc0d5cf9/30adcbef76094b3612c39857a2cc7cd98d109d33.jpg");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        TextView titleView = findViewById(R.id.title_tv);
        BannerView bannerView = findViewById(R.id.bannerView);

        titleView.setText(getIntent().getStringExtra(EXTRA_TITLE));
        int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        setBannerView(position, titleView, bannerView);
    }

    private void setBannerView(int position, TextView titleView, BannerView bannerView) {
        // bind data
        bannerView.bindData(mImageList);

        // set item click listener
        bannerView.setOnBannerItemClickListener(mOnBannerItemClickListener);
        switch (position) {
            case 0:
                titleView.setText("循环+自动滚动+scale动画");
                // 设置动画
                bannerView.setPageTransformer(new ScaleInTransformer());
                // 自动滚动
                bannerView.setIsAutoLoop(true);
                // 无线循环
                bannerView.setIsInfinite(true);
                break;
            case 1:
                titleView.setText("不循环+不自动滚动");
                // 设置动画
                bannerView.setPageTransformer(new NonPageTransformer());
                // 自动滚动
                bannerView.setIsAutoLoop(false);
                // 无线循环
                bannerView.setIsInfinite(false);
                break;
            case 2:
                titleView.setText("循环+组合动画");
                // 设置动画
                bannerView.setPageTransformer(new RotateUpPageTransformer(new ScaleInTransformer(new AlphaPageTransformer())));
                break;
            case 3:
                titleView.setText("循环+多重动画+设置边距+图片距离+滚动速度");
                // 设置动画
                bannerView.setPageTransformer(new RotateYTransformer(new ScaleInTransformer(new AlphaPageTransformer())));
                // 设置边距
                bannerView.setPageMargin(100);
                // 设置内间距
                bannerView.setPagePadding(30);
                // 设置滚动速度
                bannerView.setScrollerSpeed(500);
                break;
            case 4:
                titleView.setText("不要滚动标识点");
                bannerView.setDotVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

