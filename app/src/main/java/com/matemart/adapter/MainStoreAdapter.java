//package com.matemart.adapter;
//
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.viewpager.widget.ViewPager;
//
//import com.android.volley.VolleyError;
//import com.bumptech.glide.Glide;
//
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
//public class MainStoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    ArrayList<CardDataModel> cardList;
//    private Context mContext;
//    SharedPreference pref;
//
//
//    public MainStoreAdapter(ArrayList<CardDataModel> cardList, Context context) {
//        this.cardList = cardList;
//        this.mContext = context;
//        pref = new SharedPreference(mContext);
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View view;
//        switch (viewType) {
//            case 0:
//                view = layoutInflater.inflate(R.layout.item_type_carousel_sliderview, parent, false);
//                return new ItemCarouselSliderHolder(view);
//
//
//            case 2:
//                view = layoutInflater.inflate(R.layout.item_type_upcoming_festival, parent, false);
//                return new ItemUpcomingFestivalsHolder(view);
//
//
//            case 3:
//                view = layoutInflater.inflate(R.layout.item_typeworship_view, parent, false);
//                return new ItemWorshipLocationHolder(view);
//
//
//            case 4:
//                view = layoutInflater.inflate(R.layout.item_type_breaking_news, parent, false);
//                return new ItemBreakingNewsHolder(view);
//
//
//            case 6:
//                view = layoutInflater.inflate(R.layout.item_type_sponsor_link, parent, false);
//                return new ItemSponsoredLinkHolder(view);
//
//
//            case 7:
//                view = layoutInflater.inflate(R.layout.item_type_banner, parent, false);
//                return new ItemBannerViewHolder(view);
//
//            case 8:
//                view = layoutInflater.inflate(R.layout.item_type_back_drop_view, parent, false);
//                return new ItemBackDropViewHolder(view);
//
//            case -1:
//                view = layoutInflater.inflate(R.layout.item_type_banner, parent, false);
//                return new ItemEmptyView(view);
//
//            case 1:
//                //Today's Festival will show as default
//                view = layoutInflater.inflate(R.layout.item_type_today_festival, parent, false);
//                return new ItemTodayFestivalsHolder(view);
//
//            default:
//                view = layoutInflater.inflate(R.layout.item_type_default_card_view, parent, false);
//                return new ItemDefaultCardOneHolder(view);
//
//        }
//
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        String type = cardList.get(position).getViewID();
//        switch (type) {
//            case "carouselSlider":
//                if (cardList.get(position).getViewList().size() > 0) {
//                    holder.itemView.setVisibility(View.VISIBLE);
//                    int height_r = 16, width_r = 9;
//                    int finalHeight_r;
//                    int finalWidth_r;
//
//                    if (cardList.get(position).getImageRatio() != null || !cardList.get(position).getImageRatio().equalsIgnoreCase("")) {
//                        String aspect_ratio = cardList.get(position).getImageRatio();
//                        String[] separated = aspect_ratio.split(":");
//                        height_r = Integer.parseInt(separated[0]);
//                        width_r = Integer.parseInt(separated[1]);
//
//                    }
//                    if (height_r >= width_r) {
//                        finalHeight_r = height_r;
//                        finalWidth_r = width_r;
//                    } else {
//                        finalHeight_r = width_r;
//                        finalWidth_r = height_r;
//                    }
//
//                    ((ItemCarouselSliderHolder) holder).image_slider.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            int width = ((ItemCarouselSliderHolder) holder).image_slider.getWidth();
//                            int height = ((ItemCarouselSliderHolder) holder).image_slider.getWidth() * finalWidth_r / finalHeight_r;
//
//                            ((ItemCarouselSliderHolder) holder).image_slider.findViewById(R.id.view_pager).setLayoutParams(new RelativeLayout.LayoutParams(width, height));
//                            ((ViewPager) ((ItemCarouselSliderHolder) holder).image_slider.findViewById(R.id.view_pager)).setOffscreenPageLimit(1);
//                            ArrayList<SlideModel> imageList = new ArrayList<>();
//                            for (int i = 0; i < cardList.get(position).getViewList().size(); i++) {
//                                imageList.add(new SlideModel(cardList.get(position).getViewList().get(i).getImageURL(), ScaleTypes.FIT));
//                            }
//                            ((ItemCarouselSliderHolder) holder).image_slider.setImageList(position, imageList, ScaleTypes.FIT, ((ItemCarouselSliderHolder) holder).sliderListner);
//                        }
//                    });
//                } else {
//                    holder.itemView.setVisibility(View.GONE);
//                }
//
//                break;
//
//            case "upcomingFestivals":
//                if (cardList.get(position).getViewList().size() > 0) {
//                    holder.itemView.setVisibility(View.VISIBLE);
//                    ((ItemUpcomingFestivalsHolder) holder).tv_title.setText(cardList.get(position).getTitle());
//
//                    if (cardList.get(position).getIcon() == null || cardList.get(position).getIcon().isEmpty() || cardList.get(position).getIcon().equalsIgnoreCase("")) {
//                        ((ItemUpcomingFestivalsHolder) holder).iv_icon.setVisibility(View.GONE);
//                    } else {
//                        ((ItemUpcomingFestivalsHolder) holder).iv_icon.setVisibility(View.VISIBLE);
//                        Glide.with(mContext).load(cardList.get(position).getIcon()).placeholder(R.drawable.img_loading_image).into(((ItemUpcomingFestivalsHolder) holder).iv_icon);
//                    }
//                    LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
//                    ((ItemUpcomingFestivalsHolder) holder).rc_upcomingFestival.setLayoutManager(layoutManager);
//                    UpcomingFestivalAdapter adapter = new UpcomingFestivalAdapter(cardList.get(position).getViewList(), mContext);
//                    ((ItemUpcomingFestivalsHolder) holder).rc_upcomingFestival.setAdapter(adapter);
//                } else {
//                    holder.itemView.setVisibility(View.GONE);
//                }
//
//                break;
//
//            case "worshipLocation":
//                if (cardList.get(position).getViewList().size() > 0) {
//                    holder.itemView.setVisibility(View.VISIBLE);
//                    ((ItemWorshipLocationHolder) holder).tv_title.setText(cardList.get(position).getTitle());
//
//                    if (cardList.get(position).getIcon() == null || cardList.get(position).getIcon().isEmpty() || cardList.get(position).getIcon().equalsIgnoreCase("")) {
//                        ((ItemWorshipLocationHolder) holder).iv_icon.setVisibility(View.GONE);
//                    } else {
//                        ((ItemWorshipLocationHolder) holder).iv_icon.setVisibility(View.VISIBLE);
//                        Glide.with(mContext).load(cardList.get(position).getIcon()).placeholder(R.drawable.img_loading_image).into(((ItemWorshipLocationHolder) holder).iv_icon);
//                    }
//                    LinearLayoutManager layoutManager_worship = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
//                    ((ItemWorshipLocationHolder) holder).rc_temples.setLayoutManager(layoutManager_worship);
//                    WorshipLocationAdapter temple_adapter = new WorshipLocationAdapter(cardList.get(position).getViewList(), mContext);
//                    ((ItemWorshipLocationHolder) holder).rc_temples.setAdapter(temple_adapter);
//                } else {
//                    holder.itemView.setVisibility(View.GONE);
//                }
//
//                if (cardList.get(position).getViewAll() != null) {
//                    ((ItemWorshipLocationHolder) holder).tv_view_all.setVisibility(View.VISIBLE);
//                } else {
//                    ((ItemWorshipLocationHolder) holder).tv_view_all.setVisibility(View.GONE);
//                }
//
//                ((ItemWorshipLocationHolder) holder).tv_view_all.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (cardList.get(position).getViewAll() != null)
//                            Utils.getMediaData(mContext, pref.getString(KEY_CCID), pref.getString(KEY_PROFILE_ID), pref.getString(KEY_THEME_ID), cardList.get(position).getViewAll().getClickID(), ((ItemWorshipLocationHolder) holder), false, true);
//                    }
//                });
//                break;
//
//            case "breakingNews":
//                if (cardList.get(position).getViewList().size() > 0) {
//                    holder.itemView.setVisibility(View.VISIBLE);
//                    LinearLayoutManager layoutManager_news = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
//                    ((ItemBreakingNewsHolder) holder).rc_breaking_news.setLayoutManager(layoutManager_news);
//                    BreakingNewsAdapter breakingnews_adapter = new BreakingNewsAdapter(cardList.get(position).getViewList(), mContext);
//                    ((ItemBreakingNewsHolder) holder).rc_breaking_news.setAdapter(breakingnews_adapter);
//
//
//                    if (cardList.get(position).getViewAll() != null) {
//                        ((ItemBreakingNewsHolder) holder).tv_view_all.setVisibility(View.VISIBLE);
//                    } else {
//                        ((ItemBreakingNewsHolder) holder).tv_view_all.setVisibility(View.GONE);
//                    }
//
//                    ((ItemBreakingNewsHolder) holder).tv_view_all.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (cardList.get(position).getViewAll() != null)
//                                Utils.getMediaData(mContext, pref.getString(KEY_CCID), pref.getString(KEY_PROFILE_ID), pref.getString(KEY_THEME_ID), cardList.get(position).getViewAll().getClickID(), ((ItemBreakingNewsHolder) holder), false, true);
//                        }
//                    });
//
//                } else {
//                    holder.itemView.setVisibility(View.GONE);
//                }
//                break;
//
//            case "sponsoredLink":
//                if (cardList.get(position).getViewList().size() > 0) {
//                    holder.itemView.setVisibility(View.VISIBLE);
//                    ((ItemSponsoredLinkHolder) holder).tv_title.setText(cardList.get(position).getTitle());
//                    if (cardList.get(position).getIcon() == null || cardList.get(position).getIcon().isEmpty() || cardList.get(position).getIcon().equalsIgnoreCase("")) {
//                        ((ItemSponsoredLinkHolder) holder).iv_icon.setVisibility(View.GONE);
//                    } else {
//                        ((ItemSponsoredLinkHolder) holder).iv_icon.setVisibility(View.VISIBLE);
//                        Glide.with(mContext).load(cardList.get(position).getIcon()).placeholder(R.drawable.img_loading_image).into(((ItemSponsoredLinkHolder) holder).iv_icon);
//
//                    }
//                    LinearLayoutManager layoutManager_sponsor = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
//                    ((ItemSponsoredLinkHolder) holder).rc_sponsor.setLayoutManager(layoutManager_sponsor);
//                    SponsorLinksAdapter adapter_sponsor = new SponsorLinksAdapter(cardList.get(position).getViewList(), mContext);
//                    ((ItemSponsoredLinkHolder) holder).rc_sponsor.setAdapter(adapter_sponsor);
//                } else {
//                    holder.itemView.setVisibility(View.GONE);
//                }
//
//                break;
//
//            case "bannerView":
//
//                int height_banner = 16, width_banner = 9;
//                int finalHeight_banner;
//                int finalWidth_banner;
//                if (cardList.get(position).getImageRatio() != null || !cardList.get(position).getImageRatio().equalsIgnoreCase("")) {
//                    String aspect_ratio = cardList.get(position).getImageRatio();
//                    String[] separated = aspect_ratio.split(":");
//                    height_banner = Integer.parseInt(separated[0]);
//                    width_banner = Integer.parseInt(separated[1]);
//
//                }
//
//                if (height_banner >= width_banner) {
//                    finalHeight_banner = height_banner;
//                    finalWidth_banner = width_banner;
//                } else {
//                    finalHeight_banner = width_banner;
//                    finalWidth_banner = height_banner;
//                }
//
//                ((ItemBannerViewHolder) holder).iv_banner.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        int width = ((ItemBannerViewHolder) holder).iv_banner.getWidth();
//                        int height = ((ItemBannerViewHolder) holder).iv_banner.getWidth() * finalWidth_banner / finalHeight_banner;
//
//                        float density = mContext.getResources().getDisplayMetrics().density;
//                        float px = width * density;
//                        float dp = width / density;
//                        float px1 = height * density;
//                        float dp1 = height / density;
//                        Log.e("checkkkkk-->", "run: width in pixel:" + px + "===in dp:" + dp + "====       height:  in pixel:" + px1 + "===in dp: " + dp1);
//                        ((ItemBannerViewHolder) holder).iv_banner.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
//                        Glide.with(mContext).load(cardList.get(position).getImageURL()).placeholder(R.drawable.img_loading_image).into(((ItemBannerViewHolder) holder).iv_banner);
//                    }
//                });
//
//                ((ItemBannerViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        openWebURLInBrowser(cardList.get(position).getWebURL());
//                    }
//                });
//
//                break;
//
//
//            case "backdropView":
//
//                int height_backdropView = 16, width_backdropView = 9;
//                int finalHeight_backdropView;
//                int finalWidth_backdropView;
//                if (cardList.get(position).getImageRatio() != null || !cardList.get(position).getImageRatio().equalsIgnoreCase("")) {
//                    String aspect_ratio = cardList.get(position).getImageRatio();
//                    String[] separated = aspect_ratio.split(":");
//                    height_backdropView = Integer.parseInt(separated[0]);
//                    width_backdropView = Integer.parseInt(separated[1]);
//
//                }
//
//                if (height_backdropView >= width_backdropView) {
//                    finalHeight_backdropView = height_backdropView;
//                    finalWidth_backdropView = width_backdropView;
//                } else {
//                    finalHeight_backdropView = width_backdropView;
//                    finalWidth_backdropView = height_backdropView;
//                }
//
//                ((ItemBackDropViewHolder) holder).iv_back_drop_view.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        int width = ((ItemBackDropViewHolder) holder).iv_back_drop_view.getWidth();
//                        int height = ((ItemBackDropViewHolder) holder).iv_back_drop_view.getWidth() * finalWidth_backdropView / finalHeight_backdropView;
//
//                        ((ItemBackDropViewHolder) holder).iv_back_drop_view.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
//                        Glide.with(mContext).load(cardList.get(position).getImageURL()).placeholder(R.drawable.img_loading_image).into(((ItemBackDropViewHolder) holder).iv_back_drop_view);
//                    }
//                });
//
//
//                break;
//            case "todayFestivals":
//
//                if (cardList.get(position).getViewList().size() > 0) {
//                    holder.itemView.setVisibility(View.VISIBLE);
//                    ((ItemTodayFestivalsHolder) holder).tv_title.setText(cardList.get(position).getTitle());
//                    ((ItemTodayFestivalsHolder) holder).tv_date.setText(cardList.get(position).getDate());
//
//                    if (cardList.get(position).getIcon() == null || cardList.get(position).getIcon().isEmpty() || cardList.get(position).getIcon().equalsIgnoreCase("")) {
//                        ((ItemTodayFestivalsHolder) holder).iv_icon.setVisibility(View.GONE);
//                    } else {
//                        ((ItemTodayFestivalsHolder) holder).iv_icon.setVisibility(View.VISIBLE);
//                        Glide.with(mContext).load(cardList.get(position).getIcon()).placeholder(R.drawable.img_loading_image).into(((ItemTodayFestivalsHolder) holder).iv_icon);
//                    }
//
//                    LinearLayoutManager layoutManager_today = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
//                    ((ItemTodayFestivalsHolder) holder).rc_upcomingFestival.setLayoutManager(layoutManager_today);
//                    TodayFestivalAdapter adapter = new TodayFestivalAdapter(cardList.get(position).getViewList(), mContext);
//                    ((ItemTodayFestivalsHolder) holder).rc_upcomingFestival.setAdapter(adapter);
//                } else {
//                    holder.itemView.setVisibility(View.GONE);
//                }
//                break;
//            default:
//                if (cardList.get(position).getViewList().size() > 0) {
//                    holder.itemView.setVisibility(View.VISIBLE);
//                    ((ItemDefaultCardOneHolder) holder).tv_title.setText(cardList.get(position).getTitle());
//                    if (cardList.get(position).getIcon() == null || cardList.get(position).getIcon().isEmpty() || cardList.get(position).getIcon().equalsIgnoreCase("")) {
//                        ((ItemDefaultCardOneHolder) holder).iv_icon.setVisibility(View.GONE);
//                    } else {
//                        ((ItemDefaultCardOneHolder) holder).iv_icon.setVisibility(View.VISIBLE);
//                        Glide.with(mContext).load(cardList.get(position).getIcon()).placeholder(R.drawable.img_loading_image).into(((ItemDefaultCardOneHolder) holder).iv_icon);
//
//                    }
//                    LinearLayoutManager layoutManager_default = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
//                    ((ItemDefaultCardOneHolder) holder).rc_default.setLayoutManager(layoutManager_default);
//                    DefaultCardAdapter adapter_default = new DefaultCardAdapter(cardList.get(position).getViewList(), mContext);
//                    ((ItemDefaultCardOneHolder) holder).rc_default.setAdapter(adapter_default);
//
//
//                    if (cardList.get(position).getViewAll() != null) {
//                        ((ItemDefaultCardOneHolder) holder).tv_view_all.setVisibility(View.VISIBLE);
//                    } else {
//                        ((ItemDefaultCardOneHolder) holder).tv_view_all.setVisibility(View.GONE);
//                    }
//
//                    ((ItemDefaultCardOneHolder) holder).tv_view_all.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (cardList.get(position).getViewAll() != null)
//                                Utils.getMediaData(mContext, pref.getString(KEY_CCID), pref.getString(KEY_PROFILE_ID), pref.getString(KEY_THEME_ID), cardList.get(position).getViewAll().getClickID(), ((ItemDefaultCardOneHolder) holder), false, true);
//                        }
//                    });
//
//                } else {
//                    holder.itemView.setVisibility(View.GONE);
//                }
//                break;
//        }
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return cardList == null ? 0 : cardList.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        String type = cardList.get(position).getViewID();
//
//        switch (type) {
//            case "carouselSlider":
//                return 0;
//
//            case "todayFestivals":
//                if (cardList.get(position).getViewList().size() > 0)
//                    return 1;
//                else
//                    return -1;
//
//            case "upcomingFestivals":
//                return 2;
//
//            case "worshipLocation":
//                return 3;
//
//            case "breakingNews":
//                return 4;
//
//            case "sponsoredLink":
//                return 6;
//
//            case "bannerView":
//                return 7;
//
//            case "backdropView":
//                return 8;
//
//
//            default:
//                return 5;
//        }
//    }
//
//    public class ItemCarouselSliderHolder extends RecyclerView.ViewHolder implements SliderItemClickListner {
//        ImageSlider image_slider;
//        SliderItemClickListner sliderListner;
//
//        public ItemCarouselSliderHolder(View itemView) {
//            super(itemView);
//            image_slider = itemView.findViewById(R.id.image_slider);
//            itemView.setVisibility(View.GONE);
//            sliderListner = this;
//
//        }
//
//
//        @Override
//        public void ItemClick(int cardPosition, int position) {
//            String webURL = cardList.get(cardPosition).getViewList().get(position).getWebURL();
//            if (webURL != null && !webURL.equalsIgnoreCase("null") && !webURL.equalsIgnoreCase("") && !webURL.isEmpty()) {
//                openWebURLInBrowser(webURL);
//            }
//        }
//    }
//
//    public class ItemEmptyView extends RecyclerView.ViewHolder {
//
//        public ItemEmptyView(View itemView) {
//            super(itemView);
//
//        }
//
//    }
//
//    public class ItemTodayFestivalsHolder extends RecyclerView.ViewHolder {
//        TextView tv_title, tv_date;
//        ImageView iv_icon;
//        LinearLayout ll_header_title;
//        RecyclerView rc_upcomingFestival;
//
//        public ItemTodayFestivalsHolder(View itemView) {
//            super(itemView);
//            ll_header_title = itemView.findViewById(R.id.ll_header_title);
//            rc_upcomingFestival = itemView.findViewById(R.id.rc_upcomingFestival);
//            tv_title = itemView.findViewById(R.id.tv_title);
//            tv_date = itemView.findViewById(R.id.tv_date);
//            iv_icon = itemView.findViewById(R.id.iv_icon);
//            itemView.setVisibility(View.GONE);
//        }
//
//    }
//
//    public class ItemUpcomingFestivalsHolder extends RecyclerView.ViewHolder {
//        TextView tv_title;
//        ImageView iv_icon;
//        LinearLayout ll_header_title;
//        RecyclerView rc_upcomingFestival;
//
//        public ItemUpcomingFestivalsHolder(View itemView) {
//            super(itemView);
//            ll_header_title = itemView.findViewById(R.id.ll_header_title);
//            rc_upcomingFestival = itemView.findViewById(R.id.rc_upcomingFestival);
//            tv_title = itemView.findViewById(R.id.tv_title);
//            iv_icon = itemView.findViewById(R.id.iv_icon);
//            itemView.setVisibility(View.GONE);
//        }
//
//    }
//
//
//    public class ItemWorshipLocationHolder extends RecyclerView.ViewHolder implements HttpListener {
//        TextView tv_title;
//        ImageView iv_icon;
//        RelativeLayout ll_header_title;
//        RecyclerView rc_temples;
//        TextView tv_view_all;
//
//        public ItemWorshipLocationHolder(View itemView) {
//            super(itemView);
//            ll_header_title = itemView.findViewById(R.id.ll_header_title);
//            rc_temples = itemView.findViewById(R.id.rc_temples);
//            tv_title = itemView.findViewById(R.id.tv_title);
//            iv_icon = itemView.findViewById(R.id.iv_icon);
//            tv_view_all = itemView.findViewById(R.id.tv_view_all);
//            itemView.setVisibility(View.GONE);
//        }
//
//
//        @Override
//        public void onResponseReceived(JSONObject response, String clickID, boolean isCategoryUpdate, boolean updateGridData) {
//            Intent intent = new Intent(mContext, ContentDataActivity.class);
//            try {
//
//                String message = response.optString("message");
//                String statusCode = response.optString("statusCode");
//
//                if (statusCode.equalsIgnoreCase("11")) {
//                    String headerTitle = response.optString("headerTitle");
//                    JSONObject contentObj = response.optJSONObject("content");
//
//                    String URL = contentObj.optString("mediaURL");
//                    String ratio = contentObj.optString("mediaRatio");
//                    String mediaType = contentObj.optString("mediaType");
//
//                    intent.putExtra("headerTitle", headerTitle);
//                    intent.putExtra("mediaType", mediaType);
//                    intent.putExtra("URL", URL);
//                    intent.putExtra("ratio", ratio);
//                    intent.putExtra("clickID", clickID);
//                    mContext.startActivity(intent);
//
//                } else if (statusCode.equalsIgnoreCase("22")) {
//                    new Toaster.Builder(mContext)
//                            .setTitle("Warning")
//                            .setDescription(message)
//                            .setDuration(5000)
//                            .setStatus(Toaster.Status.WARNING)
//                            .show();
//                } else {
//                    new Toaster.Builder(mContext)
//                            .setTitle("Error")
//                            .setDescription(message)
//                            .setDuration(5000)
//                            .setStatus(Toaster.Status.ERROR)
//                            .show();
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                new Toaster.Builder(mContext)
//                        .setTitle(ERROR_TITLE)
//                        .setDescription(ERROR_MESSAGE)
//                        .setDuration(5000)
//                        .setStatus(Toaster.Status.ERROR)
//                        .show();
//
//            }
//        }
//
//        @Override
//        public void onError(VolleyError error) {
//
//        }
//    }
//
//    public class ItemBreakingNewsHolder extends RecyclerView.ViewHolder implements HttpListener {
//        TextView tv_title;
//        ImageView iv_icon;
//        RelativeLayout ll_header_title;
//        RecyclerView rc_breaking_news;
//        TextView tv_view_all;
//
//        public ItemBreakingNewsHolder(View itemView) {
//            super(itemView);
//            ll_header_title = itemView.findViewById(R.id.ll_header_title);
//            rc_breaking_news = itemView.findViewById(R.id.rc_breaking_news);
//            tv_title = itemView.findViewById(R.id.tv_title);
//            iv_icon = itemView.findViewById(R.id.iv_icon);
//            tv_view_all = itemView.findViewById(R.id.tv_view_all);
//            itemView.setVisibility(View.GONE);
//        }
//
//        @Override
//        public void onResponseReceived(JSONObject response, String clickID, boolean isCategoryUpdate, boolean updateGridData) {
//            Intent intent = new Intent(mContext, ContentDataActivity.class);
//            try {
//
//                String message = response.optString("message");
//                String statusCode = response.optString("statusCode");
//
//                if (statusCode.equalsIgnoreCase("11")) {
//                    String headerTitle = response.optString("headerTitle");
//                    JSONObject contentObj = response.optJSONObject("content");
//
//                    String URL = contentObj.optString("mediaURL");
//                    String ratio = contentObj.optString("mediaRatio");
//                    String mediaType = contentObj.optString("mediaType");
//
//                    intent.putExtra("headerTitle", headerTitle);
//                    intent.putExtra("mediaType", mediaType);
//                    intent.putExtra("URL", URL);
//                    intent.putExtra("ratio", ratio);
//                    intent.putExtra("clickID", clickID);
//                    mContext.startActivity(intent);
//
//                } else if (statusCode.equalsIgnoreCase("22")) {
//                    new Toaster.Builder(mContext)
//                            .setTitle("Warning")
//                            .setDescription(message)
//                            .setDuration(5000)
//                            .setStatus(Toaster.Status.WARNING)
//                            .show();
//                } else {
//                    new Toaster.Builder(mContext)
//                            .setTitle("Error")
//                            .setDescription(message)
//                            .setDuration(5000)
//                            .setStatus(Toaster.Status.ERROR)
//                            .show();
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                new Toaster.Builder(mContext)
//                        .setTitle(ERROR_TITLE)
//                        .setDescription(ERROR_MESSAGE)
//                        .setDuration(5000)
//                        .setStatus(Toaster.Status.ERROR)
//                        .show();
//
//            }
//        }
//
//        @Override
//        public void onError(VolleyError error) {
//
//        }
//    }
//
//    public class ItemDefaultCardOneHolder extends RecyclerView.ViewHolder implements HttpListener {
//
//        TextView tv_title;
//        ImageView iv_icon;
//        LinearLayout ll_header_title;
//        RecyclerView rc_default;
//        TextView tv_view_all;
//
//        public ItemDefaultCardOneHolder(View itemView) {
//            super(itemView);
//            ll_header_title = itemView.findViewById(R.id.ll_header_title);
//            rc_default = itemView.findViewById(R.id.rc_default);
//            tv_title = itemView.findViewById(R.id.tv_title);
//            iv_icon = itemView.findViewById(R.id.iv_icon);
//            tv_view_all = itemView.findViewById(R.id.tv_view_all);
//            itemView.setVisibility(View.GONE);
//        }
//
//        @Override
//        public void onResponseReceived(JSONObject response, String clickID, boolean isCategoryUpdate, boolean updateGridData) {
//            Intent intent = new Intent(mContext, ContentDataActivity.class);
//            try {
//
//                String message = response.optString("message");
//                String statusCode = response.optString("statusCode");
//
//                if (statusCode.equalsIgnoreCase("11")) {
//                    String headerTitle = response.optString("headerTitle");
//                    JSONObject contentObj = response.optJSONObject("content");
//
//                    String URL = contentObj.optString("mediaURL");
//                    String ratio = contentObj.optString("mediaRatio");
//                    String mediaType = contentObj.optString("mediaType");
//
//                    intent.putExtra("headerTitle", headerTitle);
//                    intent.putExtra("mediaType", mediaType);
//                    intent.putExtra("URL", URL);
//                    intent.putExtra("ratio", ratio);
//                    intent.putExtra("clickID", clickID);
//                    mContext.startActivity(intent);
//
//                } else if (statusCode.equalsIgnoreCase("22")) {
//                    new Toaster.Builder(mContext)
//                            .setTitle("Warning")
//                            .setDescription(message)
//                            .setDuration(5000)
//                            .setStatus(Toaster.Status.WARNING)
//                            .show();
//                } else {
//                    new Toaster.Builder(mContext)
//                            .setTitle("Error")
//                            .setDescription(message)
//                            .setDuration(5000)
//                            .setStatus(Toaster.Status.ERROR)
//                            .show();
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                new Toaster.Builder(mContext)
//                        .setTitle(ERROR_TITLE)
//                        .setDescription(ERROR_MESSAGE)
//                        .setDuration(5000)
//                        .setStatus(Toaster.Status.ERROR)
//                        .show();
//
//            }
//
//        }
//
//        @Override
//        public void onError(VolleyError error) {
//
//        }
//    }
//
//    public class ItemSponsoredLinkHolder extends RecyclerView.ViewHolder {
//
//        TextView tv_title;
//        ImageView iv_icon;
//        LinearLayout ll_header_title;
//        RecyclerView rc_sponsor;
//
//        public ItemSponsoredLinkHolder(View itemView) {
//            super(itemView);
//            ll_header_title = itemView.findViewById(R.id.ll_header_title);
//            rc_sponsor = itemView.findViewById(R.id.rc_sponsor);
//            tv_title = itemView.findViewById(R.id.tv_title);
//            iv_icon = itemView.findViewById(R.id.iv_icon);
//            itemView.setVisibility(View.GONE);
//        }
//
//    }
//
//    public class ItemBannerViewHolder extends RecyclerView.ViewHolder {
//        ImageView iv_banner;
//
//        public ItemBannerViewHolder(View itemView) {
//            super(itemView);
//            iv_banner = itemView.findViewById(R.id.iv_banner);
//        }
//
//    }
//
//
//    public class ItemBackDropViewHolder extends RecyclerView.ViewHolder {
//        ImageView iv_back_drop_view;
//
//        public ItemBackDropViewHolder(View itemView) {
//            super(itemView);
//            iv_back_drop_view = itemView.findViewById(R.id.iv_back_drop_view);
//        }
//
//    }
//
//    public void openWebURLInBrowser(String webURL) {
//        Intent intent = new Intent(mContext, WebViewActivity.class);
//        intent.putExtra("webURL", webURL);
//        mContext.startActivity(intent);
//
//    }
//}