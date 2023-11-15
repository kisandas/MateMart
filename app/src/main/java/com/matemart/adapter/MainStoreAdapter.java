package com.matemart.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.matemart.R;
import com.matemart.activities.CategoryListActivity;
import com.matemart.activities.SearchProductFromCategoryActivity;
import com.matemart.activities.SubCategoryActivityList;
import com.matemart.interfaces.SliderItemClickListner;
import com.matemart.interfaces.WishListUpdateListner;
import com.matemart.model.HomeDetailModel;
import com.matemart.utils.ImageSlider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class MainStoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements WishListUpdateListner {

    ArrayList<HomeDetailModel> cardList;
    private Context mContext;

    public MainStoreAdapter(ArrayList<HomeDetailModel> cardList, Context context) {
        this.cardList = cardList;
        this.mContext = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            case 0:
                view = layoutInflater.inflate(R.layout.item_type_carousel_sliderview, parent, false);
                return new ItemCarouselSliderHolder(view);

            case 1:
                view = layoutInflater.inflate(R.layout.item_type_home_product_list, parent, false);
                return new ItemHomeProductListHolder(view);

            case 2:
                view = layoutInflater.inflate(R.layout.item_type_home_category_list, parent, false);
                return new ItemTopCategoryHolder(view);


            case 3:
                view = layoutInflater.inflate(R.layout.item_home_banner, parent, false);
                return new ItemBannerViewHolder(view);

            case -1:
                view = layoutInflater.inflate(R.layout.item_empty, parent, false);
                return new EmptyViewHolder(view);

            default:
                view = layoutInflater.inflate(R.layout.item_home_banner2, parent, false);
                return new ItemBanner2ViewHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String type = cardList.get(position).getViewId();
        switch (type) {
            case "carouselSlider":
                if (cardList.get(position).getViewList().size() > 0) {
                    holder.itemView.setVisibility(View.VISIBLE);
                    int height_r = 16, width_r = 9;
                    int finalHeight_r;
                    int finalWidth_r;

                    if (cardList.get(position).getImageRatio() != null || !cardList.get(position).getImageRatio().equalsIgnoreCase("")) {
                        String aspect_ratio = cardList.get(position).getImageRatio();
                        String[] separated = aspect_ratio.split(":");
                        height_r = Integer.parseInt(separated[0]);
                        width_r = Integer.parseInt(separated[1]);

                    }
                    if (height_r >= width_r) {
                        finalHeight_r = height_r;
                        finalWidth_r = width_r;
                    } else {
                        finalHeight_r = width_r;
                        finalWidth_r = height_r;
                    }

                    ((ItemCarouselSliderHolder) holder).image_slider.post(new Runnable() {
                        @Override
                        public void run() {
                            int width = ((ItemCarouselSliderHolder) holder).image_slider.getWidth();
                            int height = ((ItemCarouselSliderHolder) holder).image_slider.getWidth() * finalWidth_r / finalHeight_r;

                            ((ItemCarouselSliderHolder) holder).image_slider.findViewById(R.id.view_pager).setLayoutParams(new RelativeLayout.LayoutParams(width, height));
                            ((ViewPager) ((ItemCarouselSliderHolder) holder).image_slider.findViewById(R.id.view_pager)).setOffscreenPageLimit(1);
                            ArrayList<SlideModel> imageList = new ArrayList<>();
                            for (int i = 0; i < cardList.get(position).getViewList().size(); i++) {
                                imageList.add(new SlideModel(cardList.get(position).getViewList().get(i).getImage(), ScaleTypes.FIT));
                            }
                            ((ItemCarouselSliderHolder) holder).image_slider.setImageList(position, imageList, ScaleTypes.FIT, ((ItemCarouselSliderHolder) holder).sliderListner);
                        }
                    });
                } else {
                    holder.itemView.setVisibility(View.GONE);
                }

                break;


            case "product_list":
                if (cardList.get(position).getViewList().size() > 0) {
                    holder.itemView.setVisibility(View.VISIBLE);

                    ((ItemHomeProductListHolder) holder).tv_title.setText(cardList.get(position).getTitle());
                    LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                    ((ItemHomeProductListHolder) holder).rcHomeProductList.setLayoutManager(layoutManager);
                    ProductItemAdapter adapter = new ProductItemAdapter(new ArrayList(cardList.get(position).getViewList()), mContext, this);
                    ((ItemHomeProductListHolder) holder).rcHomeProductList.setAdapter(adapter);

                    if (cardList.get(position).getViewAll() != null && cardList.get(position).getViewAll().getTotal_record() > 0) {
                        ((ItemHomeProductListHolder) holder).tv_viewAll.setVisibility(View.VISIBLE);
                    } else {
                        ((ItemHomeProductListHolder) holder).tv_viewAll.setVisibility(View.GONE);

                    }

                    ((ItemHomeProductListHolder) holder).tv_viewAll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mContext.startActivity(new Intent(mContext, SearchProductFromCategoryActivity.class)
                                    .putExtra("clickId", cardList.get(position).getViewAll().getClickId())
                                    .putExtra("title", cardList.get(position).getTitle()));
                        }
                    });

                } else {
                    holder.itemView.setVisibility(View.GONE);
                }


                break;


            case "top_categories":
                if (cardList.get(position).getViewList().size() > 0) {
                    holder.itemView.setVisibility(View.VISIBLE);
                    ((ItemTopCategoryHolder) holder).tv_title.setText(cardList.get(position).getTitle());
                    LinearLayoutManager layoutManager_worship = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                    ((ItemTopCategoryHolder) holder).rcHomeCategoryList.setLayoutManager(layoutManager_worship);
                    HomeCategoryAdapter temple_adapter = new HomeCategoryAdapter(new ArrayList(cardList.get(position).getViewList()), mContext);
                    ((ItemTopCategoryHolder) holder).rcHomeCategoryList.setAdapter(temple_adapter);
                    if (cardList.get(position).getViewAll() != null && cardList.get(position).getViewAll().getTotal_record() > 0) {
                        ((ItemTopCategoryHolder) holder).tv_view_all.setVisibility(View.VISIBLE);
                    } else {
                        ((ItemTopCategoryHolder) holder).tv_view_all.setVisibility(View.GONE);
                    }

                    ((ItemTopCategoryHolder) holder).tv_view_all.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mContext.startActivity(new Intent(mContext, CategoryListActivity.class).putExtra("clickId", cardList.get(position).getViewAll().getClickId()).putExtra("title", cardList.get(position).getTitle()));

                        }
                    });
                } else {
                    holder.itemView.setVisibility(View.GONE);
                }


                break;

            case "banner_view_1":

                int height_banner = 16, width_banner = 9;
                int finalHeight_banner;
                int finalWidth_banner;

                if (cardList.get(position).getImageRatio() != null || !cardList.get(position).getImageRatio().equalsIgnoreCase("")) {
                    String aspect_ratio = cardList.get(position).getImageRatio();
                    String[] separated = aspect_ratio.split(":");
                    height_banner = Integer.parseInt(separated[0]);
                    width_banner = Integer.parseInt(separated[1]);

                }

                if (height_banner >= width_banner) {
                    finalHeight_banner = height_banner;
                    finalWidth_banner = width_banner;
                } else {
                    finalHeight_banner = width_banner;
                    finalWidth_banner = height_banner;
                }

                ((ItemBannerViewHolder) holder).tv_title.setText(cardList.get(position).getTitle());
                ((ItemBannerViewHolder) holder).iv_banner.post(new Runnable() {
                    @Override
                    public void run() {
                        int width = ((ItemBannerViewHolder) holder).iv_banner.getWidth();
                        int height = ((ItemBannerViewHolder) holder).iv_banner.getWidth() * finalWidth_banner / finalHeight_banner;

                        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(width, height);
                        param.addRule(RelativeLayout.BELOW, R.id.tv_title);
                        param.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        param.topMargin = 10;

                        ((ItemBannerViewHolder) holder).iv_banner.setLayoutParams(param);
                        Glide.with(mContext).load(cardList.get(position).getImage()).placeholder(R.drawable.img_loading_image).into(((ItemBannerViewHolder) holder).iv_banner);
                    }
                });

                ((ItemBannerViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cardList.get(position).getClickId() != null && !cardList.get(position).getClickId().isEmpty())
                            mContext.startActivity(new Intent(mContext, SearchProductFromCategoryActivity.class)
                                    .putExtra("clickId", cardList.get(position).getClickId())
                                    .putExtra("title", cardList.get(position).getTitle()));
                    }
                });


                break;


            case "banner_view_2":
                int height_banner2 = 16, width_banner2 = 9;
                int finalHeight_banner2;
                int finalWidth_banner2;
                if (cardList.get(position).getImageRatio() != null || !cardList.get(position).getImageRatio().equalsIgnoreCase("")) {
                    String aspect_ratio = cardList.get(position).getImageRatio();
                    String[] separated = aspect_ratio.split(":");
                    height_banner2 = Integer.parseInt(separated[0]);
                    width_banner2 = Integer.parseInt(separated[1]);

                }

                if (height_banner2 >= width_banner2) {
                    finalHeight_banner2 = height_banner2;
                    finalWidth_banner2 = width_banner2;
                } else {
                    finalHeight_banner2 = width_banner2;
                    finalWidth_banner2 = height_banner2;
                }

                ((ItemBanner2ViewHolder) holder).iv_banner.post(new Runnable() {
                    @Override
                    public void run() {
                        int width = ((ItemBanner2ViewHolder) holder).iv_banner.getWidth();
                        int height = ((ItemBanner2ViewHolder) holder).iv_banner.getWidth() * finalWidth_banner2 / finalHeight_banner2;

                        ((ItemBanner2ViewHolder) holder).iv_banner.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
                        Glide.with(mContext).load(cardList.get(position).getImage()).placeholder(R.drawable.img_loading_image).into(((ItemBanner2ViewHolder) holder).iv_banner);
                    }
                });

                ((ItemBanner2ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!Objects.equals(cardList.get(position).getClickId(), "null") && cardList.get(position).getClickId() != null && !cardList.get(position).getViewAll().getClickId().isEmpty())
                            mContext.startActivity(new Intent(mContext, SearchProductFromCategoryActivity.class)
                                    .putExtra("clickId", cardList.get(position).getClickId())
                                    .putExtra("title", cardList.get(position).getTitle()));
                    }
                });

                break;
        }

    }

    public static Object convertStringToIntOrReturnOriginal(String input) {
        try {
            int intValue = Integer.parseInt(input);
            return intValue; // Return the integer value
        } catch (NumberFormatException e) {
            return input; // Return the original string
        }
    }


    @Override
    public int getItemCount() {
        return cardList == null ? 0 : cardList.size();
    }

    @Override
    public int getItemViewType(int position) {
        String type = cardList.get(position).getViewId();

        switch (type) {
            case "carouselSlider":
                return 0;

            case "product_list":
                if (cardList.get(position).getViewList().size() > 0)
                    return 1;
                else
                    return -1;

            case "top_categories":
                return 2;

            case "banner_view_1":
                return 3;

            default:
                return 4;
        }
    }

    @Override
    public void onUpdate() {

    }

    public class ItemCarouselSliderHolder extends RecyclerView.ViewHolder implements SliderItemClickListner {
        ImageSlider image_slider;
        SliderItemClickListner sliderListner;

        public ItemCarouselSliderHolder(View itemView) {
            super(itemView);
            image_slider = itemView.findViewById(R.id.image_slider);
            itemView.setVisibility(View.GONE);
            sliderListner = this;

        }


        @Override
        public void ItemClick(int cardPosition, int position) {
//            String webURL = cardList.get(cardPosition).getViewList().get(position).getWebURL();
//            if (webURL != null && !webURL.equalsIgnoreCase("null") && !webURL.equalsIgnoreCase("") && !webURL.isEmpty()) {
//                openWebURLInBrowser(webURL);
//            }
        }
    }


    public class ItemHomeProductListHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_viewAll;
        RecyclerView rcHomeProductList;

        public ItemHomeProductListHolder(View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_viewAll = itemView.findViewById(R.id.tv_viewAll);
            rcHomeProductList = itemView.findViewById(R.id.rcHomeProductList);
            itemView.setVisibility(View.GONE);
        }

    }


    public class ItemTopCategoryHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_view_all;

        RecyclerView rcHomeCategoryList;

        public ItemTopCategoryHolder(View itemView) {
            super(itemView);

            rcHomeCategoryList = itemView.findViewById(R.id.rcHomeCategoryList);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_view_all = itemView.findViewById(R.id.tv_viewAll);

            itemView.setVisibility(View.GONE);
        }


    }


    public class ItemBannerViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_banner;
        TextView tv_title;

        public ItemBannerViewHolder(View itemView) {
            super(itemView);
            iv_banner = itemView.findViewById(R.id.ivBanner);
            tv_title = itemView.findViewById(R.id.tv_title);
        }


    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {


        public EmptyViewHolder(View itemView) {
            super(itemView);

        }


    }


    public class ItemBanner2ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_banner;

        public ItemBanner2ViewHolder(View itemView) {
            super(itemView);
            iv_banner = itemView.findViewById(R.id.ivBanner);
        }


    }


    public void openWebURLInBrowser(String webURL) {
//        Intent intent = new Intent(mContext, WebViewActivity.class);
//        intent.putExtra("webURL", webURL);
//        mContext.startActivity(intent);

    }
}