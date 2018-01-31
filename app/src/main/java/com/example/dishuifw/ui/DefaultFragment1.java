package com.example.dishuifw.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.dishuifw.BaseFragment;
import com.example.dishuifw.MyApplication;
import com.example.dishuifw.R;
import com.example.dishuifw.core.constant.ProtocolConstant.ServerStatus;
import com.example.dishuifw.util.LoadSdCardBitmapUtil;
import com.example.dishuifw.widget.HeaderLoopViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/***
 * 空闲默认显示的界面
 * **/
public class DefaultFragment1 extends BaseFragment implements OnClickListener {


    private final int switchImgTime = 5 * 1000;

    private HeaderLoopViewPager mViewPager;
    private ImageView vDefaultImage;

    private static int initPositon = 50000;
    private int currentPosition = initPositon;

    private DisplayImageOptions options;
    public ImageLoader imageLoader = ImageLoader.getInstance();
    private HomeImgBackgroundAdapter mBgAdapter;
    private LayoutInflater mInflater;
    private List<String> files = new ArrayList<String>();
    private Animation mHiddenAction;

    private Handler mLoadImgHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (files.size() > 0) {
                mBgAdapter.appendToList(files);
                mHandler.sendEmptyMessage(0);
                hideDefaultImg();
            }
        }

        ;
    };

    // Handler对象更新UI
    Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            currentPosition++;
            if (currentPosition >= Integer.MAX_VALUE) {
                currentPosition = initPositon;
            }
            mViewPager.setCurrentItem(currentPosition);
            mHandler.sendEmptyMessageDelayed(0, switchImgTime);
        }
    };

    public static DefaultFragment1 getInstance() {
        return new DefaultFragment1();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflater = getActivity().getLayoutInflater();
        mHiddenAction = AnimationUtils.loadAnimation(getActivity(), R.anim.defalut_img_exit);
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.default_bg)
                .showImageOnFail(R.drawable.default_bg)
                .resetViewBeforeLoading(true).cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        if (ImageLoaderConfiguration.createDefault(getActivity()) != null) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        }
        mRootView = inflater.inflate(R.layout.fragment_default1, container,
                false);
        init();
        return mRootView;
    }


    private void init() {

        vDefaultImage = (ImageView) findView(R.id.home_imageView_defaultBg);
        mViewPager = (HeaderLoopViewPager) findView(R.id.home_viewPager_imgContainer);

        mBgAdapter = new HomeImgBackgroundAdapter();
        mViewPager.setAdapter(mBgAdapter);

        mViewPager
                .setOnPageChangeListener(new com.example.dishuifw.widget.YohoBuyViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int position) {
                        currentPosition = position;
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {
                        // TODO 此时考虑是否加入动画 alipahAnimaOut( R.anim.alpha_out);
                    }
                });

        mViewPager.setOnTouchListener(new MyTouchListener());

        new Thread() {
            @Override
            public void run() {
                files = LoadSdCardBitmapUtil.getImgsInFoloder();
                mLoadImgHandler.sendEmptyMessage(1);
            }
        }.start();

    }

    @Override
    public void onPause() {
        mHandler.removeMessages(0);
        super.onPause();
    }

    /**
     * 监听手势监听器
     *
     * @author user
     */
    class MyTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:

                    mHandler.removeMessages(0);
                    break;
                case MotionEvent.ACTION_UP:

                default:
                    mHandler.sendEmptyMessageDelayed(0, 4000);
                    break;
            }
            return false;
        }

    }

    public class HomeImgBackgroundAdapter extends PagerAdapter {

        public final List<String> mImgPaths = new LinkedList<String>();

        public HomeImgBackgroundAdapter() {
        }

        public void appendToList(List<String> list) {
            if (list == null) {
                return;
            }
            mImgPaths.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // container.removeView(mImgPaths.get(position%mImgPaths.size()));//
            // 删除页卡
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
            View imageLayout = mInflater.inflate(R.layout.item_pager_image,
                    container, false);
            try {

                assert imageLayout != null;
                ImageView imageView = (ImageView) imageLayout
                        .findViewById(R.id.image);
                imageLoader.displayImage(
                        mImgPaths.get(position % mImgPaths.size()), imageView,
                        options);
                container.addView(imageLayout, 0);

                // 添加页卡
            } catch (Exception e) {

            }
            return imageLayout;
        }

        @Override
        public int getCount() {
            if (mImgPaths.size() == 0)
                return 0;
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;// 官方提示这样写
        }

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handleMsgUpdateToView() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void initComponents(View rootView) {
        // TODO Auto-generated method stub

    }

    private void hideDefaultImg() {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                vDefaultImage.startAnimation(mHiddenAction);
                vDefaultImage.setVisibility(View.GONE);
            }
        }, 6000);

    }

}
